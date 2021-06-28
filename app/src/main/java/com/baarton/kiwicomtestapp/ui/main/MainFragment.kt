package com.baarton.kiwicomtestapp.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

//    val url = "https://api.skypicker.com/flights?flyFrom=PRG&to=LGW&dateFrom=18/11/2020&dateTo=12/12/2020&v=3&partner=ondrejbartinterviewappsolution1"

    private lateinit var queue: RequestQueue

    val url = "https://api.skypicker.com/flights?flyFrom=PRG&to=LGW&dateFrom=29/06/2021&dateTo=04/07/2021&partner=ondrejbartinterviewappsolution1&v=3"


    lateinit var btn: Button
    lateinit var textView: TextView


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel


        btn = view.findViewById(R.id.btn_load)
        textView = view.findViewById(R.id.message)

        queue = MySingleton.getInstance(this.requireContext()).requestQueue

        btn.setOnClickListener { loadData() }

    }

    private fun loadData() {
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                textView.text = "Response is:\n${response.substring(0, 500)}"
            },
            Response.ErrorListener { error -> textView.text = "That didn't work!\nError:\n${error.networkResponse}" })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)


    }

}