package com.baarton.kiwicomtestapp.db

import android.content.Context
import androidx.room.*
import com.baarton.kiwicomtestapp.app.IDatabaseModule
import com.baarton.kiwicomtestapp.data.Flight


class DatabaseModule(context: Context): IDatabaseModule {

    override val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "flights")
        .fallbackToDestructiveMigration().build() // NOTE: destructive migration is nothing we want in production

    @Database(entities = [Flight::class], version = 6)
    @TypeConverters(Converters::class)
    abstract class AppDatabase : RoomDatabase() {

        abstract fun flightDao(): FlightDao

    }

}