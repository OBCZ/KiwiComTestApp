package com.baarton.kiwicomtestapp.app

import android.app.Application
import com.baarton.kiwicomtestapp.db.DatabaseModule
import com.baarton.kiwicomtestapp.network.RequestHandler
import com.baarton.kiwicomtestapp.network.ResponseHandler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startModules()
    }

    private fun startModules() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            val appModule = AppModule.get(
                RequestHandler(this.koin.get()),
                ResponseHandler(),
                DatabaseModule(this.koin.get())
            )

            modules(appModule)
        }
    }

}