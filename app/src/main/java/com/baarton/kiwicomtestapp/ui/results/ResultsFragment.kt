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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.db.fromDb
import com.baarton.kiwicomtestapp.db.toDb
import com.baarton.kiwicomtestapp.request.RequestHelper
import com.baarton.kiwicomtestapp.response.ResponseParser
import com.baarton.kiwicomtestapp.ui.StartFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.util.logging.Level
import java.util.logging.Logger


class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()

        private val logger = Logger.getLogger(ResultsFragment::class.java.name)
    }

    private lateinit var db: AppDatabase

    private lateinit var viewModel: ResultsViewModel

    private lateinit var overviewTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flightsList: RecyclerView
    private lateinit var btnBack: MaterialButton
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
        btnBack = view.findViewById(R.id.btn_back)

        flightsList.layoutManager = LinearLayoutManager(requireContext())
        flightsList.itemAnimator = DefaultItemAnimator()
        flightsList.adapter = flightsAdapter

        btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, StartFragment.newInstance())
                .commitNow()
        }

        viewModel = ViewModelProvider(this).get(ResultsViewModel::class.java)

        observeLiveData()
        loadData()
    }

    private fun observeLiveData() {
        viewModel.overviewText.observe(viewLifecycleOwner, { newText -> overviewTextView.text = newText })
        viewModel.infoText.observe(viewLifecycleOwner, { newText -> infoTextView.text = newText })
        viewModel.infoTextVisibility.observe(viewLifecycleOwner, { newVisibility -> infoTextView.visibility = newVisibility })
        viewModel.progressBarVisibility.observe(viewLifecycleOwner, { newVisibility -> progressBar.visibility = newVisibility })
    }

    private fun loadData() {
        setLoadingInfo()

        var dbFlights: List<com.baarton.kiwicomtestapp.db.Flight>? = null
        val readDbJob = lifecycleScope.launch {
            logger.log(Level.INFO, "Querying data from the DB: START")
            dbFlights = db.flightDao().getAll()
            logger.log(Level.INFO, "Querying data from the DB: DONE")
        }

        readDbJob.invokeOnCompletion {
            logger.log(Level.INFO, "DB data collection check. DB returned collection of size ${dbFlights!!.size}")
            if (dbFlights!!.isNotEmpty()) {
                //TODO
                // > check last saved date from shared prefs
                //   > if new day, save the db data to a variable [DONE], nuke DB, and request new data, minus queried data, and take 5
                //   > if same day use DB data
                refreshAdapterData(dbFlights!!.map { dbFlight -> fromDb(dbFlight) })
            } else {
                requestData()
            }
        }
    }

    private fun refreshAdapterData(data: List<Flight>) {
        flightsAdapter.flights = data
        flightsAdapter.notifyDataSetChanged()
        setLoadingComplete()
        logger.log(Level.INFO, "UI fed.")
    }

    private fun requestData() {
        logger.log(Level.INFO, "Queuing request.")
        RequestHelper.queueRequest(
            { response ->
                logger.log(Level.INFO, "Got response with length of ${response.length} characters.")
                val result = ResponseParser.parse(response)
                logger.log(Level.INFO, "Response parsed.")
                refreshAdapterData(result)

                 lifecycleScope.launch {
                     logger.log(Level.INFO, "Inserting data to the DB: START")
                     db.flightDao().insertAll(*result.map {
                         flight: Flight -> toDb(flight)
                     }.toTypedArray())
                     logger.log(Level.INFO, "Inserting data to the DB: DONE")
                 }

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