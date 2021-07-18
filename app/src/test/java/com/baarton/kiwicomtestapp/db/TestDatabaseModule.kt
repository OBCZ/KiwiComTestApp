package com.baarton.kiwicomtestapp.db

import android.content.Context
import androidx.room.Room
import com.baarton.kiwicomtestapp.app.IDatabaseModule


class TestDatabaseModule(context: Context): IDatabaseModule {

    override val db = Room.inMemoryDatabaseBuilder(context,
        DatabaseModule.AppDatabase::class.java).build()

}