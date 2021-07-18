package com.baarton.kiwicomtestapp.network

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.baarton.kiwicomtestapp.app.IRequestHandler
import java.util.logging.Level
import java.util.logging.Logger


class RequestHandler(context: Context): IRequestHandler {

    companion object {
        private val logger = Logger.getLogger(RequestHandler::class.java.name)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }.also {
        logger.log(Level.INFO, "Request queue object initialized.")
    }

    override val imageLoader: ImageLoader? by lazy {
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

    override fun queueRequest(request: Request<*>) {
        requestQueue.add(request)
        logger.log(Level.INFO, "Request queued: ${request.tag}.")
    }

    override fun cancelQueue() {
        requestQueue.cancelAll { true }
        logger.log(Level.INFO, "Cancelling all queued requests.")
    }

}