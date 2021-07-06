package com.baarton.kiwicomtestapp.ui.results

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.db.toDb
import com.baarton.kiwicomtestapp.request.RequestHelper
import com.baarton.kiwicomtestapp.response.ResponseParser
import kotlinx.coroutines.*


class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()
    }

    lateinit var db: AppDatabase

    private lateinit var viewModel: ResultsViewModel

    private lateinit var overviewTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flightsList: RecyclerView
    private val flightsAdapter: FlightsAdapter = FlightsAdapter(listOf())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        RequestHelper.initQueue(context)
        db = AppDatabase.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.results_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overviewTextView = view.findViewById(R.id.overview_results)
        infoTextView = view.findViewById(R.id.overview_info_text)
        progressBar = view.findViewById(R.id.overview_progress_bar)
        flightsList = view.findViewById(R.id.list_results)

        flightsList.layoutManager = LinearLayoutManager(requireContext())
        flightsList.itemAnimator = DefaultItemAnimator()
        flightsList.adapter = flightsAdapter

        viewModel = ViewModelProvider(this).get(ResultsViewModel::class.java)

        observeLiveData()

        //TODO if DB data present
        // > if so, check last saved date from shared prefs
        //   > if new day, nuke DB and request new data
        //   > if same day use DB data
        // > if not, request new data
        requestData()
    }

    private fun observeLiveData() {
        viewModel.overviewText.observe(viewLifecycleOwner, { newText -> overviewTextView.text = newText })
        viewModel.infoText.observe(viewLifecycleOwner, { newText -> infoTextView.text = newText })
        viewModel.infoTextVisibility.observe(viewLifecycleOwner, { newVisibility -> infoTextView.visibility = newVisibility })
        viewModel.progressBarVisibility.observe(viewLifecycleOwner, { newVisibility -> progressBar.visibility = newVisibility })
    }

    private fun requestData() {
        setLoadingInfo()
        RequestHelper.queueRequest(
            { response ->
                val result = ResponseParser.parse(response)
                flightsAdapter.flights = result
                flightsAdapter.notifyDataSetChanged()
                setLoadingComplete()

                //TODO musim vytvorit vlastni coroutine context?
                 runBlocking { launch {
                    db.flightDao().insertAll(*result.map {
                        flight: Flight -> toDb(flight)
                    }.toTypedArray())
                } }

                //TODO save today'S date to shared prefs

            },
            { error ->
                setLoadingError(error)
            }
        )
    }

    private fun setLoadingError(error: VolleyError) {
        viewModel.progressBarVisibility.value = View.GONE
        viewModel.infoTextVisibility.value = View.VISIBLE
        viewModel.infoText.value = "That didn't work!\nError:\n${error.message}"
    }

    private fun setLoadingComplete() {
        viewModel.progressBarVisibility.value = View.GONE
        viewModel.infoTextVisibility.value = View.GONE
        viewModel.overviewText.value = "Showing 5 most popular destinations for today"
    }

    private fun setLoadingInfo() {
        viewModel.progressBarVisibility.value = View.VISIBLE
        viewModel.infoTextVisibility.value = View.GONE
    }

    override fun onStop() {
        super.onStop()
        RequestHelper.cancel()
    }

}