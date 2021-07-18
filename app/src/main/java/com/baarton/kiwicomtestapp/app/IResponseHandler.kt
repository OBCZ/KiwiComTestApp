package com.baarton.kiwicomtestapp.app

import com.baarton.kiwicomtestapp.data.FlightData

interface IResponseHandler {
    fun parse(response: String): FlightData
}