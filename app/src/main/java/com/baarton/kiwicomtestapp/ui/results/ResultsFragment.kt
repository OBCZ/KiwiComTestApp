package com.baarton.kiwicomtestapp.ui.results

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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.network.MySingleton
import org.json.JSONObject
import java.util.logging.Logger


class ResultsFragment : Fragment() {

    companion object {
        fun newInstance() = ResultsFragment()

        val logger = Logger.getLogger("KIWISEARCH")

    }

    private lateinit var viewModel: ResultsViewModel

    private lateinit var queue: RequestQueue

    //TODO build the url request dynamically regarding the dates (current day + 1)
    val url = "https://api.skypicker.com/flights?flyFrom=PRG&dateFrom=29/06/2021&dateTo=30/06/2021&partner=ondrejbartinterviewappsolution1&v=3"


    private lateinit var overviewTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var flightsList: RecyclerView
    private val flightsAdapter: FlightsAdapter = FlightsAdapter(listOf())


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

        queue = MySingleton.getInstance(this.requireContext()).requestQueue

        loadData() //TODO if not any DB data present?
    }

    private fun clearData() {
        updateLiveData("NO DATA LOADED")
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

    private fun loadData() {
        setLoadingInfo()

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
//                val jObject = JSONObject(response)
//                logger.log(Level.INFO, jObject.toString())
                val dataArray = JSONObject(response).getJSONArray("data")  //TODO catch JSONException?
//                logger.log(Level.INFO, "Result count: ${dataArray.length()}")

           //     updateLiveData("Response is:\n${response.take(500)}")

                val resultsToParse = mutableListOf<JSONObject>()
                for (i in 0..4) {
                    resultsToParse.add(dataArray.getJSONObject(i)) //TODO catch JSONException?
                }

                parse(resultsToParse)

                //TODO parse result
                setLoadingComplete(resultsToParse.size, dataArray.length())

            },
            Response.ErrorListener { error ->
                setLoadingError(error)
               // updateLiveData("That didn't work!\nError:\n${error.networkResponse}")
            }
        )
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    //TODO extract somehow to separate parser class
    private fun parse(resultsToParse: MutableList<JSONObject>) {
        val flights = mutableListOf<Flight>()
        for (jsonObject in resultsToParse) { //TODO catch JSONException?
            flights.add(Flight(
                    jsonObject.getString("id"),
                    jsonObject.getString("dTime"),
                    jsonObject.getString("aTime"),
                    jsonObject.getString("fly_duration"),
                    jsonObject.getString("flyFrom"),
                    jsonObject.getString("cityFrom"),
                    jsonObject.getString("flyTo"),
                    jsonObject.getString("cityTo"),
                    jsonObject.getString("price")
            ))
        }
        flightsAdapter.flights = flights
        flightsAdapter.notifyDataSetChanged()
    }

    private fun setLoadingError(error: VolleyError) {
        viewModel.progressBarVisibility.value = View.GONE
        viewModel.infoTextVisibility.value = View.VISIBLE
        viewModel.infoText.value = "That didn't work!\nError:\n${error.networkResponse}"
    }

    private fun setLoadingComplete(chosenLength: Int, totalLength: Int) {
        viewModel.progressBarVisibility.value = View.GONE
        viewModel.infoTextVisibility.value = View.GONE
        viewModel.overviewText.value = "Showing results: $chosenLength/$totalLength"
    }

    private fun setLoadingInfo() {
        viewModel.progressBarVisibility.value = View.VISIBLE
        viewModel.infoTextVisibility.value = View.GONE
    }

    private fun updateLiveData(response: String) {
        viewModel.infoText.value = response
    }

}