package com.baarton.kiwicomtestapp.ui.results

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.app.IDatabaseModule
import com.baarton.kiwicomtestapp.app.IRequestHandler
import com.baarton.kiwicomtestapp.app.IResponseHandler
import com.baarton.kiwicomtestapp.data.Flight
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
    internal val overviewTextVisibility = MutableLiveData<Int>()
    internal val progressBarVisibility = MutableLiveData<Int>()
    internal val flightListData = MutableLiveData<List<Flight>>()

    private val databaseModule: IDatabaseModule by fragment.inject()
    private val requestHandler: IRequestHandler by fragment.inject()
    private val responseHandler: IResponseHandler by fragment.inject()

    private var flights: List<Flight> = listOf()

    init {
        overviewText.value = ""
        overviewTextVisibility.value = View.GONE
        infoText.value = fragment.getString(R.string.text_results_nothing)
        infoTextVisibility.value = View.VISIBLE
        progressBarVisibility.value = View.GONE

        loadData()
    }

    private fun loadData() {
        setLoadingInfo()

        val readDbJob = fragment.lifecycleScope.launch {
            logger.log(Level.INFO, "Querying data from the DB: START")
            flights = databaseModule.db.flightDao().getAll()
            logger.log(Level.INFO, "Querying data from the DB: DONE")
        }

        readDbJob.invokeOnCompletion {
            logger.log(Level.INFO, "DB data collection check. DB returned collection of size ${flights.size}.")
            if (flights.isNotEmpty()) {
                if (flights[0].dbDate == LocalDate.now().format(DATE_FORMAT)) {
                    logger.log(Level.INFO, "DB data will be displayed.")
                    flightListData.value = flights
                    setLoadingComplete()
                } else {
                    logger.log(Level.INFO, "New data will be requested.")
                    fragment.lifecycleScope.launch {
                        logger.log(Level.INFO, "Nuking the DB: START")
                        databaseModule.db.flightDao().nuke()
                        logger.log(Level.INFO, "Nuking the DB: END")
                    }.invokeOnCompletion {
                        requestData()
                    }
                }
            } else {
                logger.log(Level.INFO, "New data will be requested.")
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
                val responseFlightData = responseHandler.parse(response)
                val responseFlights = responseFlightData.list
                val responseCurrency = responseFlightData.currency

                flights = responseFlights.filter { !oldFlightIds.contains(it.flightId) }.take(MAX_ITEM_COUNT)
                logger.log(Level.INFO, "Response parsed.")

                flights.onEach {
                    it.dbDate = LocalDate.now().format(DATE_FORMAT)
                    it.currency = responseCurrency
                }

                flightListData.value = flights
                setLoadingComplete()

                fragment.lifecycleScope.launch {
                    logger.log(Level.INFO, "Inserting data to the DB: START")
                    databaseModule.db.flightDao().insertAll(*flights.toTypedArray())
                    logger.log(Level.INFO, "Inserting data to the DB: DONE")
                }
            },
            { error ->
                setLoadingError(error)
            }
        )
        stringRequest.tag = DATA_REQUEST_TAG
        requestHandler.queueRequest(stringRequest)
    }

    private fun constructQueryUrl(): String {
        val now = LocalDate.now().format(DATE_FORMAT)
        val tomorrow = LocalDate.now().plusDays(1L).format(DATE_FORMAT)
        return String.format(DATA_REQUEST_URL, now, tomorrow)
    }

    private fun setLoadingError(error: VolleyError) {
        progressBarVisibility.value = View.GONE
        overviewTextVisibility.value = View.GONE
        infoTextVisibility.value = View.VISIBLE
        infoText.value = fragment.getString(R.string.text_results_error)
        logger.log(Level.SEVERE, "Got an error on the data request: ${error.message}")
    }

    private fun setLoadingComplete() {
        progressBarVisibility.value = View.GONE
        if (flights.isNotEmpty()) {
            overviewTextVisibility.value = View.VISIBLE
            overviewText.value = fragment.getString(R.string.text_results_heading, flights.size)
            infoTextVisibility.value = View.GONE
        } else {
            overviewTextVisibility.value = View.GONE
            overviewText.value = ""
            infoTextVisibility.value = View.VISIBLE
            infoText.value = fragment.getString(R.string.text_results_nothing)
        }
    }

    private fun setLoadingInfo() {
        progressBarVisibility.value = View.VISIBLE
        overviewTextVisibility.value = View.GONE
        infoTextVisibility.value = View.GONE
    }

}