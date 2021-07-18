package com.baarton.kiwicomtestapp.app

import com.baarton.kiwicomtestapp.db.DatabaseModule

interface IDatabaseModule {
    val db: DatabaseModule.AppDatabase
}