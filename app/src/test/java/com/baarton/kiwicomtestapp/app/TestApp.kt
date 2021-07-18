package com.baarton.kiwicomtestapp.app

import android.app.Application
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.db.TestDatabaseModule
import com.baarton.kiwicomtestapp.network.FakeRequestHandler
import com.baarton.kiwicomtestapp.network.FakeResponseHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_KiwiComTestApp)
        startModules()
    }

    private fun startModules() {
        startKoin {
            androidContext(this@TestApp)
            val appModule = AppModule.get(
                FakeRequestHandler(),
                FakeResponseHandler(),
                TestDatabaseModule(this.koin.get())
            )

            modules(appModule)
        }
    }

}