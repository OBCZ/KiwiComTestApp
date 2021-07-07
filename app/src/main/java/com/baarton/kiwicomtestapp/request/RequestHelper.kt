package com.baarton.kiwicomtestapp.request

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.baarton.kiwicomtestapp.network.RequestQueueSingle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger


object RequestHelper {

    private val logger = Logger.getLogger(RequestHelper::class.java.name)

    private const val DATA_REQUEST_TAG = "KIWI_DATA_REQUEST"
    private const val URL = "https://api.skypicker.com/flights?flyFrom=PRG&dateFrom=%s&dateTo=%s&partner=ondrejbartinterviewappsolution1&v=3"

    private var queue: RequestQueue? = null
    private var imageLoader: ImageLoader? = null

    fun initQueue(context: Context) {
        queue = RequestQueueSingle.getInstance(context).requestQueue
        imageLoader = RequestQueueSingle.getInstance(context).imageLoader
        logger.log(Level.INFO, "Request queue object initialized.")
    }

    fun queueRequest(responseListener: Response.Listener<String>, errorListener: Response.ErrorListener) {
        val stringRequest = StringRequest(Request.Method.GET, constructQueryUrl(), responseListener, errorListener)
        stringRequest.tag = DATA_REQUEST_TAG
        queue?.add(stringRequest)
        logger.log(Level.INFO, "Request queued: $DATA_REQUEST_TAG.")
    }

    private fun constructQueryUrl(): String {
        val now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val tomorrow = LocalDate.now().plusDays(1L).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return String.format(URL, now, tomorrow)
    }

    fun cancel() {
        queue?.cancelAll(DATA_REQUEST_TAG)
        logger.log(Level.INFO, "Cancelling request with tag (if queued): $DATA_REQUEST_TAG.")
    }

    fun getImageLoader(): ImageLoader? {
        return imageLoader
    }

}