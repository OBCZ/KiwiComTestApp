package com.baarton.kiwicomtestapp.db

import android.content.Context
import androidx.room.Room
import com.baarton.kiwicomtestapp.app.IDatabaseModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestDatabaseModule(context: Context): IDatabaseModule {

    override val coroutineDispatcher = TestCoroutineDispatcher()

    override val db = Room.inMemoryDatabaseBuilder(context,
        DatabaseModule.AppDatabase::class.java)
        .setTransactionExecutor(coroutineDispatcher.asExecutor())
        .setQueryExecutor(coroutineDispatcher.asExecutor())
        .allowMainThreadQueries()
        .build()

}