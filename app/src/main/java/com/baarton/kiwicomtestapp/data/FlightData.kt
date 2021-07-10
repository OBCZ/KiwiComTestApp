package com.baarton.kiwicomtestapp.data

import com.google.gson.annotations.SerializedName


data class FlightData (
    @SerializedName("data")
    var list: List<Flight>,
    @SerializedName("currency")
    val currency: String,
)