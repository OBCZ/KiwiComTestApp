package com.baarton.kiwicomtestapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Flight::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "flights"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    abstract fun flightDao(): FlightDao

}