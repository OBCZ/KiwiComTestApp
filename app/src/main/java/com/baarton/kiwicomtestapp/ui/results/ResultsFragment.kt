package com.baarton.kiwicomtestapp.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.network.RequestService
import com.baarton.kiwicomtestapp.ui.StartFragment
import com.google.android.material.button.MaterialButton
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.logging.Level
import java.util.logging.Logger


class ResultsFragment : Fragment() {

    companion object {
        private val logger = Logger.getLogger(ResultsFragment::class.java.name)

        fun newInstance() = ResultsFragment()
    }

    private val requestService: RequestService by inject()
    private val resultsViewModel: ResultsViewModel by viewModel { parametersOf(this) }

    private lateinit var overviewTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flightsList: RecyclerView
    private lateinit var btnBack: MaterialButton
    private val flightsAdapter: FlightsAdapter = FlightsAdapter(listOf(), requestService.imageLoader)

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

        observeLiveData()
    }

    private fun observeLiveData() {
        resultsViewModel.overviewText.observe(viewLifecycleOwner, { newText -> overviewTextView.text = newText })
        resultsViewModel.infoText.observe(viewLifecycleOwner, { newText -> infoTextView.text = newText })
        resultsViewModel.infoTextVisibility.observe(viewLifecycleOwner, { newVisibility -> infoTextView.visibility = newVisibility })
        resultsViewModel.progressBarVisibility.observe(viewLifecycleOwner, { newVisibility -> progressBar.visibility = newVisibility })
        resultsViewModel.flightListData.observe(viewLifecycleOwner, FlightListObserver())
    }

    override fun onStop() {
        super.onStop()
        requestService.cancel()
    }

    inner class FlightListObserver : Observer<List<Flight>> {

        override fun onChanged(newList: List<Flight>) {
            flightsAdapter.flights = newList
            flightsAdapter.notifyDataSetChanged()
            logger.log(Level.INFO, "UI fed.")
        }

    }

}