package com.baarton.kiwicomtestapp.network

import com.baarton.kiwicomtestapp.data.Flight
import com.baarton.kiwicomtestapp.data.FlightData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.logging.Level
import java.util.logging.Logger


class ResponseService {

    private val logger = Logger.getLogger(ResponseService::class.java.name)

    fun parse(response: String): List<Flight> {
        val flightData: FlightData?
        try {
            flightData = Gson().fromJson(response, FlightData::class.java)
        } catch (e: JsonSyntaxException) {
            logger.log(Level.SEVERE, "Caught an exception when parsing response. Response:\n$response. Exception:\n${e.localizedMessage}")
            return emptyList()
        }

        return if (flightData == null) {
            logger.log(Level.WARNING, "Parsed response is null. Response was:\n$response")
            emptyList()
        } else {
            flightData.list!!
        }
    }
    
}