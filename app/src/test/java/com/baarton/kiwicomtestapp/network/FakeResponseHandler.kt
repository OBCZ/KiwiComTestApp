package com.baarton.kiwicomtestapp.network

import com.baarton.kiwicomtestapp.app.IResponseHandler
import com.baarton.kiwicomtestapp.data.FlightData

class FakeResponseHandler: IResponseHandler {

    override fun parse(response: String): FlightData {
        TODO("Not yet implemented")
    }

}