package com.baarton.kiwicomtestapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Flight (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "id") val flightId: String,
    @ColumnInfo(name = "dTime") val departureTime: String,
    @ColumnInfo(name = "aTime") val arrivalTime: String,
    @ColumnInfo(name = "fly_duration") val duration: String,
    @ColumnInfo(name = "flyFrom") val flyFrom: String,
    @ColumnInfo(name = "cityFrom") val cityFrom: String,
    @ColumnInfo(name = "flyTo") val flyTo: String,
    @ColumnInfo(name = "cityTo") val cityTo: String,
    @ColumnInfo(name = "price") val price: String
)