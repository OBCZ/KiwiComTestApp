package com.baarton.kiwicomtestapp.ui.results

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.request.RequestHelper
import com.baarton.kiwicomtestapp.response.ResponseParser


class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()
    }

    private lateinit var viewModel: ResultsViewModel

    private lateinit var overviewTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flightsList: RecyclerView
    private val flightsAdapter: FlightsAdapter = FlightsAdapter(listOf())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        RequestHelper.initQueue(context)
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

        requestData() //TODO if not any DB data present?
    }

    //TODO extract to view model?
    private fun observeLiveData() {
        viewModel.overviewText.observe(viewLifecycleOwner,
            Observer<String> { newText -> overviewTextView.text = newText })
        viewModel.infoText.observe(viewLifecycleOwner,
            Observer<String> { newText -> infoTextView.text = newText })
        viewModel.infoTextVisibility.observe(viewLifecycleOwner,
            Observer<Int> { newVisibility -> infoTextView.visibility = newVisibility })
        viewModel.progressBarVisibility.observe(viewLifecycleOwner,
            Observer<Int> { newVisibility -> progressBar.visibility = newVisibility })
    }

    private fun requestData() {
        setLoadingInfo()
        RequestHelper.queueRequest(
            Response.Listener { response ->
                flightsAdapter.flights = ResponseParser.parse(response)
                flightsAdapter.notifyDataSetChanged()
                setLoadingComplete()
            },
            Response.ErrorListener { error ->
                setLoadingError(error)
            }
        )
    }

    private fun setLoadingError(error: VolleyError) {
        viewModel.progressBarVisibility.value = View.GONE
        viewModel.infoTextVisibility.value = View.VISIBLE
        viewModel.infoText.value = "That didn't work!\nError:\n${error.networkResponse}"
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