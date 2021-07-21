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
import com.baarton.kiwicomtestapp.app.IRequestHandler
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.ui.StartFragment
import com.google.android.material.button.MaterialButton
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.logging.Level
import java.util.logging.Logger


class ResultsFragment : Fragment() {

    companion object {
        private val logger = Logger.getLogger(ResultsFragment::class.java.name)

        fun newInstance() = ResultsFragment()
    }

    private val requestHandler: IRequestHandler by inject()
    private val resultsViewModel: ResultsViewModel by viewModel()

    private lateinit var overviewTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flightsList: RecyclerView
    private lateinit var btnBack: MaterialButton
    private val flightsAdapter: FlightsAdapter = FlightsAdapter(listOf(), requestHandler.imageLoader)

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
        resultsViewModel.overviewTextItemAmount.observe(viewLifecycleOwner, {
                newItemAmount -> overviewTextView.text = when (newItemAmount) {
                    0 -> { getString(R.string.text_empty) }
                    else -> { getString(R.string.text_results_heading, newItemAmount) }
                }
        })
        resultsViewModel.overviewTextVisibility.observe(viewLifecycleOwner, { newVisibility -> infoTextView.visibility = newVisibility })
        resultsViewModel.infoTextRes.observe(viewLifecycleOwner, { newTextResId -> infoTextView.text = getString(newTextResId) })
        resultsViewModel.infoTextVisibility.observe(viewLifecycleOwner, { newVisibility -> infoTextView.visibility = newVisibility })
        resultsViewModel.progressBarVisibility.observe(viewLifecycleOwner, { newVisibility -> progressBar.visibility = newVisibility })
        resultsViewModel.flightListData.observe(viewLifecycleOwner, FlightListObserver())
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        resultsViewModel.loadData()
    }

    override fun onStop() {
        super.onStop()
        requestHandler.cancelQueue()
    }

    inner class FlightListObserver : Observer<List<Flight>> {

        override fun onChanged(newList: List<Flight>) {
            flightsAdapter.flights = newList
            flightsAdapter.notifyDataSetChanged()
            logger.log(Level.INFO, "UI fed.")
        }

    }

}