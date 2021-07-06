package com.baarton.kiwicomtestapp.data

import com.google.gson.annotations.SerializedName

data class Flight(
    @SerializedName("id")
    internal val flightId: String,
    @SerializedName("dTime")
    internal val departureTime: String,
    @SerializedName("aTime")
    internal val arrivalTime: String,
    @SerializedName("fly_duration")
    internal val duration: String,
    @SerializedName("flyFrom")
    internal val flyFrom: String,
    @SerializedName("cityFrom")
    internal val cityFrom: String,
    @SerializedName("flyTo")
    internal val flyTo: String,
    @SerializedName("cityTo")
    internal val cityTo: String,
    @SerializedName("price")
    internal val price: String
)