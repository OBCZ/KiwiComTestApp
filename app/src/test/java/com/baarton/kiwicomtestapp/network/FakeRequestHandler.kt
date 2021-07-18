package com.baarton.kiwicomtestapp.network

import com.android.volley.Request
import com.android.volley.toolbox.ImageLoader
import com.baarton.kiwicomtestapp.app.IRequestHandler

class FakeRequestHandler: IRequestHandler {

    override val imageLoader: ImageLoader? = null
    override fun queueRequest(request: Request<*>) {
        // nothing
    }

    override fun cancelQueue() {
        // nothing
    }

}