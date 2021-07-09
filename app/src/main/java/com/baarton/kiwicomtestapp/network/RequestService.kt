package com.baarton.kiwicomtestapp.network

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger


class RequestService(context: Context) {

    companion object {
        private val logger = Logger.getLogger(RequestService::class.java.name)
        private const val DATA_REQUEST_TAG = "KIWI_DATA_REQUEST"
        private const val URL = "https://api.skypicker.com/flights?flyFrom=PRG&dateFrom=%s&dateTo=%s&partner=ondrejbartinterviewappsolution1&v=3"
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }.also {
        logger.log(Level.INFO, "Request queue object initialized.")
    }

    val imageLoader: ImageLoader by lazy {
        ImageLoader(requestQueue,
            object : ImageLoader.ImageCache {

                private val cache = LruCache<String, Bitmap>(20)

                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }

                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            }
        )
    }.also {
        logger.log(Level.INFO, "Image loader object initialized.")
    }

    fun queueRequest(responseListener: Response.Listener<String>, errorListener: Response.ErrorListener) {
        val stringRequest = StringRequest(Request.Method.GET, constructQueryUrl(), responseListener, errorListener)
        stringRequest.tag = DATA_REQUEST_TAG
        requestQueue.add(stringRequest)
        logger.log(Level.INFO, "Request queued: $DATA_REQUEST_TAG.")
    }

    private fun constructQueryUrl(): String {
        val now = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val tomorrow = LocalDate.now().plusDays(1L).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return String.format(URL, now, tomorrow)
    }

    fun cancel() {
        requestQueue.cancelAll(DATA_REQUEST_TAG)
        logger.log(Level.INFO, "Cancelling request with tag (if queued): $DATA_REQUEST_TAG.")
    }

}