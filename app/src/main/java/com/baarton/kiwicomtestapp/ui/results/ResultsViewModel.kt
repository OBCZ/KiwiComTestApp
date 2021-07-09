package com.baarton.kiwicomtestapp.ui.results

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.network.RequestService
import com.baarton.kiwicomtestapp.network.ResponseService
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger


class ResultsViewModel(private val fragment: ResultsFragment) : ViewModel() {

    companion object {
        private val logger = Logger.getLogger(ResultsViewModel::class.java.name)

        private const val DATA_REQUEST_TAG = "KIWI_DATA_REQUEST"
        private const val DATA_REQUEST_URL = "https://api.skypicker.com/flights?flyFrom=PRG&dateFrom=%s&dateTo=%s&partner=ondrejbartinterviewappsolution1&v=3"
        private const val MAX_ITEM_COUNT = 5

        private val DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    internal val infoText = MutableLiveData<String>()
    internal val overviewText = MutableLiveData<String>()
    internal val infoTextVisibility = MutableLiveData<Int>()
    internal val progressBarVisibility = MutableLiveData<Int>()
    internal val flightListData = MutableLiveData<List<Flight>>()

    private val databaseModule: AppDatabase by fragment.inject()
    private val requestService: RequestService by fragment.inject()
    private val responseService: ResponseService by fragment.inject()

    private var flights: List<Flight> = listOf()

    init {
        overviewText.value = "NO RESULTS"
        infoText.value = "NO DATA LOADED"
        infoTextVisibility.value = View.VISIBLE
        progressBarVisibility.value = View.GONE

        loadData()
    }

    private fun loadData() {
        setLoadingInfo()

        val readDbJob = fragment.lifecycleScope.launch {
            logger.log(Level.INFO, "Querying data from the DB: START")
            flights = databaseModule.flightDao().getAll()
            logger.log(Level.INFO, "Querying data from the DB: DONE")
        }

        readDbJob.invokeOnCompletion {
            logger.log(Level.INFO, "DB data collection check. DB returned collection of size ${flights.size}")
            if (flights.isNotEmpty()) {
                if (flights[0].dbDate == LocalDate.now().format(DATE_FORMAT)) {
                    flightListData.value = flights
                    setLoadingComplete()
                } else {
                    fragment.lifecycleScope.launch {
                        databaseModule.flightDao().nuke()
                    }.invokeOnCompletion {
                        requestData()
                    }
                }
            } else {
                requestData()
            }
        }
    }

    private fun requestData() {
        logger.log(Level.INFO, "Queuing request.")

        val stringRequest = StringRequest(Request.Method.GET, constructQueryUrl(),
            { response ->
                logger.log(Level.INFO, "Got response with length of ${response.length} characters.")

                val oldFlightIds = flights.map { it.flightId }
                val responseFlights = responseService.parse(response)

                flights = responseFlights.filter { !oldFlightIds.contains(it.flightId) }.take(MAX_ITEM_COUNT)
                logger.log(Level.INFO, "Response parsed.")

                flightListData.value = flights
                setLoadingComplete()

                fragment.lifecycleScope.launch {
                    logger.log(Level.INFO, "Inserting data to the DB: START")
                    databaseModule.flightDao().insertAll(*flights.onEach { it.dbDate = LocalDate.now().format(DATE_FORMAT) }.toTypedArray())
                    logger.log(Level.INFO, "Inserting data to the DB: DONE")
                }
            },
            { error ->
                setLoadingError(error)
            }
        )
        stringRequest.tag = DATA_REQUEST_TAG
        requestService.queueRequest(stringRequest)
    }

    private fun constructQueryUrl(): String {
        val now = LocalDate.now().format(DATE_FORMAT)
        val tomorrow = LocalDate.now().plusDays(1L).format(DATE_FORMAT)
        return String.format(DATA_REQUEST_URL, now, tomorrow)
    }

    private fun setLoadingError(error: VolleyError) {
        progressBarVisibility.value = View.GONE
        infoTextVisibility.value = View.VISIBLE
        infoText.value = "That didn't work!\nError:\n${error.message}"
    }

    private fun setLoadingComplete() {
        progressBarVisibility.value = View.GONE
        infoTextVisibility.value = View.GONE
        overviewText.value = "Showing ${flights.size} most popular destinations for today"
    }

    private fun setLoadingInfo() {
        progressBarVisibility.value = View.VISIBLE
        infoTextVisibility.value = View.GONE
    }

}