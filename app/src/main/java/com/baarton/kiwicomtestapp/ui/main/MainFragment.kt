package com.baarton.kiwicomtestapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.network.MySingleton

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var queue: RequestQueue

    val url = "https://api.skypicker.com/flights?flyFrom=PRG&to=LGW&dateFrom=29/06/2021&dateTo=04/07/2021&partner=ondrejbartinterviewappsolution1&v=3"


    private lateinit var btnLoad: Button
    private lateinit var btnClear: Button
    private lateinit var textView: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLoad = view.findViewById(R.id.btn_load)
        btnClear = view.findViewById(R.id.btn_clear)
        textView = view.findViewById(R.id.message)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        observeLiveData()

        queue = MySingleton.getInstance(this.requireContext()).requestQueue

        btnLoad.setOnClickListener { loadData() }
        btnClear.setOnClickListener { clearData() }

    }

    private fun clearData() {
        updateLiveData("NO DATA LOADED")
    }

    private fun observeLiveData() {
        viewModel.liveData.observe(viewLifecycleOwner,
            Observer<String> { newText -> textView.text = newText })
    }

    private fun loadData() {
        updateLiveData("... LOADING ...")

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                updateLiveData("Response is:\n${response.substring(0, 500)}")
            },
            Response.ErrorListener { error -> updateLiveData("That didn't work!\nError:\n${error.networkResponse}") }
        )
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

    private fun updateLiveData(response: String) {
        viewModel.liveData.value = response
    }

}