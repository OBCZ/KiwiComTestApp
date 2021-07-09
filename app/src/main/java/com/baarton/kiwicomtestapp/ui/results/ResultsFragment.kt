package com.baarton.kiwicomtestapp.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.network.RequestService
import com.baarton.kiwicomtestapp.network.ResponseService
import com.baarton.kiwicomtestapp.ui.StartFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.logging.Level
import java.util.logging.Logger


class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()

        private const val MAX_ITEM_COUNT = 5
        private val logger = Logger.getLogger(ResultsFragment::class.java.name)
    }

    private val requestService: RequestService by inject()
    private val responseService: ResponseService by inject()
    private val databaseModule: AppDatabase by inject()
    private val resultsViewModel by viewModel<ResultsViewModel>()

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
        loadData()
    }

    private fun observeLiveData() {
        resultsViewModel.overviewText.observe(viewLifecycleOwner, { newText -> overviewTextView.text = newText })
        resultsViewModel.infoText.observe(viewLifecycleOwner, { newText -> infoTextView.text = newText })
        resultsViewModel.infoTextVisibility.observe(viewLifecycleOwner, { newVisibility -> infoTextView.visibility = newVisibility })
        resultsViewModel.progressBarVisibility.observe(viewLifecycleOwner, { newVisibility -> progressBar.visibility = newVisibility })
    }

    private fun loadData() {
        setLoadingInfo()

        var dbFlights: List<Flight>? = null
        val readDbJob = lifecycleScope.launch {
            logger.log(Level.INFO, "Querying data from the DB: START")
            dbFlights = databaseModule.flightDao().getAll()
            logger.log(Level.INFO, "Querying data from the DB: DONE")
        }

        readDbJob.invokeOnCompletion {
            logger.log(Level.INFO, "DB data collection check. DB returned collection of size ${dbFlights!!.size}")
            if (dbFlights!!.isNotEmpty()) {
                //TODO
                // > check last saved date from shared prefs
                //   > if new day, save the db data to a variable [DONE], nuke DB, and request new data, minus queried data, and take 5
                //   > if same day use DB data
                refreshAdapterData(dbFlights!!)
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
        requestService.queueRequest(
            { response ->
                logger.log(Level.INFO, "Got response with length of ${response.length} characters.")
                val result = responseService.parse(response).take(MAX_ITEM_COUNT)
                logger.log(Level.INFO, "Response parsed.")
                refreshAdapterData(result)

                 lifecycleScope.launch {
                     logger.log(Level.INFO, "Inserting data to the DB: START")
                     databaseModule.flightDao().insertAll(*result.toTypedArray())
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
        resultsViewModel.progressBarVisibility.value = View.GONE
        resultsViewModel.infoTextVisibility.value = View.VISIBLE
        resultsViewModel.infoText.value = "That didn't work!\nError:\n${error.message}"
    }

    private fun setLoadingComplete() {
        resultsViewModel.progressBarVisibility.value = View.GONE
        resultsViewModel.infoTextVisibility.value = View.GONE
        resultsViewModel.overviewText.value = "Showing 5 most popular destinations for today"
    }

    private fun setLoadingInfo() {
        resultsViewModel.progressBarVisibility.value = View.VISIBLE
        resultsViewModel.infoTextVisibility.value = View.GONE
    }

    override fun onStop() {
        super.onStop()
        requestService.cancel()
    }

}