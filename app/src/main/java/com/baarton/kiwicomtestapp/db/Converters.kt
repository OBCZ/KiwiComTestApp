package com.baarton.kiwicomtestapp.db

import androidx.room.TypeConverter
import com.baarton.kiwicomtestapp.data.RouteElem

//TODO test candidates
class Converters {

    @TypeConverter
    fun toRoute(string: String): List<RouteElem> {
        return string.split("|").map { routeElemStr -> RouteElem(routeElemStr) }
    }

    @TypeConverter
    fun fromRoute(route: List<RouteElem>): String {
        return route.joinToString(separator = "|") { routeElem -> routeElem.mapIdTo }
    }

}