package com.baarton.kiwicomtestapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baarton.kiwicomtestapp.data.Flight


@Database(entities = [Flight::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun flightDao(): FlightDao

}