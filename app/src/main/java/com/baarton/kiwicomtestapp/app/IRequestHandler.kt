package com.baarton.kiwicomtestapp.app

import com.android.volley.Request
import com.android.volley.toolbox.ImageLoader

interface IRequestHandler {
    val imageLoader: ImageLoader?
    fun queueRequest(request: Request<*>)
    fun cancelQueue()
}