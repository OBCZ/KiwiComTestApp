package com.baarton.kiwicomtestapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Flight::class], version = 2)
@TypeConverters(Converters::class)
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
                    ).fallbackToDestructiveMigration().build() // NOTE: destructive migration is nothing we want in production
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    abstract fun flightDao(): FlightDao

}