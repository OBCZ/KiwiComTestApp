package com.baarton.kiwicomtestapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class Flight (
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo(name = "flightId")
    val flightId: String,
    @ColumnInfo(name = "dTime")
    val departureTime: String,
    @ColumnInfo(name = "aTime")
    val arrivalTime: String,
    @ColumnInfo(name = "fly_duration")
    val duration: String,
    @ColumnInfo(name = "flyFrom")
    val flyFrom: String,
    @ColumnInfo(name = "cityFrom")
    val cityFrom: String,
    @ColumnInfo(name = "flyTo")
    val flyTo: String,
    @ColumnInfo(name = "cityTo")
    val cityTo: String,
    @ColumnInfo(name = "price")
    val price: String
)

fun toDb(flight: com.baarton.kiwicomtestapp.data.Flight): Flight {
    return Flight(
        flightId = flight.flightId,
        departureTime = flight.departureTime,
        arrivalTime = flight.arrivalTime,
        duration = flight.duration,
        flyFrom = flight.flyFrom,
        cityFrom = flight.cityFrom,
        flyTo = flight.flyTo,
        cityTo = flight.cityTo,
        price = flight.price
    )
}

fun fromDb(flight: Flight): com.baarton.kiwicomtestapp.data.Flight {
    return com.baarton.kiwicomtestapp.data.Flight(
        flightId = flight.flightId,
        departureTime = flight.departureTime,
        arrivalTime = flight.arrivalTime,
        duration = flight.duration,
        flyFrom = flight.flyFrom,
        cityFrom = flight.cityFrom,
        flyTo = flight.flyTo,
        cityTo = flight.cityTo,
        price = flight.price
    )
}