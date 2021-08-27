package com.baarton.kiwicomtestapp.network

import com.android.volley.Request
import com.android.volley.toolbox.ImageLoader
import com.baarton.kiwicomtestapp.app.IRequestHandler
import java.util.logging.Level
import java.util.logging.Logger

class FakeRequestHandler: IRequestHandler {

    companion object {
        private val logger = Logger.getLogger(FakeRequestHandler::class.java.name)
    }

    override val imageLoader: ImageLoader by lazy {
        ImageLoader(null, null)
    }.also {
        logger.log(Level.INFO, "Fake Image loader object initialized.")
    }
    override fun queueRequest(request: Request<*>) {
        logger.log(Level.INFO, "Request fake-queued: ${request.tag}.")
    }

    override fun cancelQueue() {
        logger.log(Level.INFO, "Cancelling all fake-queued requests.")
    }

}