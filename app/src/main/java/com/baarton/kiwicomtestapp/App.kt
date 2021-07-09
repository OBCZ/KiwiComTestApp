package com.baarton.kiwicomtestapp

import android.app.Application
import androidx.room.Room
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.network.RequestService
import com.baarton.kiwicomtestapp.network.ResponseService
import com.baarton.kiwicomtestapp.ui.results.ResultsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            val appModule = module {
                single { RequestService(get()) }
                single { ResponseService() }
                single {
                    Room.databaseBuilder(
                        this@App,
                        AppDatabase::class.java,
                        "flights"
                    ).fallbackToDestructiveMigration().build() // NOTE: destructive migration is nothing we want in production
                }
                viewModel { ResultsViewModel() }
            }
            modules(appModule)
        }
    }

}