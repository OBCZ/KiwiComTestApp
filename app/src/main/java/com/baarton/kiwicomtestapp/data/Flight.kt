package com.baarton.kiwicomtestapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "flights")
data class Flight(
    @PrimaryKey
    var dbId: Int? = null,
    @ColumnInfo(name = "dbDate")
    var dbDate: String? = null,
    @ColumnInfo(name = "flightId")
    @SerializedName("id")
    val flightId: String,
    @ColumnInfo(name = "dTime")
    @SerializedName("dTime")
    val departureTime: String,
    @ColumnInfo(name = "aTime")
    @SerializedName("aTime")
    val arrivalTime: String,
    @ColumnInfo(name = "fly_duration")
    @SerializedName("fly_duration")
    val duration: String,
    @ColumnInfo(name = "flyFrom")
    @SerializedName("flyFrom")
    val flyFrom: String,
    @ColumnInfo(name = "cityFrom")
    @SerializedName("cityFrom")
    val cityFrom: String,
    @ColumnInfo(name = "flyTo")
    @SerializedName("flyTo")
    val flyTo: String,
    @ColumnInfo(name = "cityTo")
    @SerializedName("cityTo")
    val cityTo: String,
    @ColumnInfo(name = "price")
    @SerializedName("price")
    val price: String,
    @ColumnInfo(name = "currency")
    var currency: String? = null,
    @SerializedName("route")
    @ColumnInfo(name = "route")
    val routeList: List<RouteElem>
)

data class RouteElem(
    @SerializedName("mapIdto")
    internal val mapIdTo: String
)