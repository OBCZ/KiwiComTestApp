package com.baarton.kiwicomtestapp.app

import com.baarton.kiwicomtestapp.db.DatabaseModule
import kotlinx.coroutines.CoroutineDispatcher

interface IDatabaseModule {
    val db: DatabaseModule.AppDatabase
    val coroutineDispatcher: CoroutineDispatcher?
        get() = null
}