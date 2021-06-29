package com.baarton.kiwicomtestapp.ui.results

data class Flight(
    internal val id: String,
    internal val departureTime: String,
    internal val arrivalTime: String,
    internal val duration: String,
    internal val flyFrom: String,
    internal val cityFrom: String,
    internal val flyTo: String,
    internal val cityTo: String,
    internal val price: String
)