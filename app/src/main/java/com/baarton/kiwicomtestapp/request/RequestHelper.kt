package com.baarton.kiwicomtestapp.request

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.baarton.kiwicomtestapp.network.RequestQueueSingle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger

object RequestHelper {

    private val logger = Logger.getLogger(RequestHelper::class.java.name)

    private const val REQUEST_TAG = "KIWI_REQUEST"
    private const val URL = "https://api.skypicker.com/flights?flyFrom=PRG&dateFrom=%s&dateTo=%s&partner=ondrejbartinterviewappsolution1&v=3"

    private var queue: RequestQueue? = null

    fun initQueue(context: Context) {
        queue = RequestQueueSingle.getInstance(context).requestQueue
        logger.log(Level.INFO, "Request queue object initialized.")
    }

    fun queueRequest(responseListener: Response.Listener<String>, errorListener: Response.ErrorListener) {
        val stringRequest = StringRequest(Request.Method.GET, constructQueryUrl(), responseListener, errorListener)
        stringRequest.tag = REQUEST_TAG
        queue?.add(stringRequest)
        logger.log(Level.INFO, "Request queued: $REQUEST_TAG.")
    }

    private fun constructQueryUrl(): String {
        val now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val tomorrow = LocalDate.now().plusDays(1L).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return String.format(URL, now, tomorrow)
    }

    fun cancel() {
        queue?.cancelAll(REQUEST_TAG)
        logger.log(Level.INFO, "Request cancelled: $REQUEST_TAG.")
    }

}