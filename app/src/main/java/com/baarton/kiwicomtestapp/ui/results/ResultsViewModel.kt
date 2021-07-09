package com.baarton.kiwicomtestapp.ui.results

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.android.volley.VolleyError
import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.network.RequestService
import com.baarton.kiwicomtestapp.network.ResponseService
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.logging.Level
import java.util.logging.Logger


class ResultsViewModel(private val fragment: ResultsFragment) : ViewModel() {

    companion object {
        private val logger = Logger.getLogger(ResultsViewModel::class.java.name)

        private const val MAX_ITEM_COUNT = 5
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
                //TODO
                // > check last saved date from shared prefs
                //   > if new day, save the db data to a variable [DONE], nuke DB, and request new data, minus queried data, and take 5
                //   > if same day use DB data
                flightListData.value = flights
                setLoadingComplete()
            } else {
                requestData()
            }
        }
    }

    private fun requestData() {
        logger.log(Level.INFO, "Queuing request.")
        requestService.queueRequest(
            { response ->
                logger.log(Level.INFO, "Got response with length of ${response.length} characters.")
                flights = responseService.parse(response).take(MAX_ITEM_COUNT)
                logger.log(Level.INFO, "Response parsed.")

                flightListData.value = flights
                setLoadingComplete()

                fragment.lifecycleScope.launch {
                    logger.log(Level.INFO, "Inserting data to the DB: START")
                    databaseModule.flightDao().insertAll(*flights.toTypedArray())
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