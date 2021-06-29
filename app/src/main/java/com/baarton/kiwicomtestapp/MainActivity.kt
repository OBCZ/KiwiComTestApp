package com.baarton.kiwicomtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baarton.kiwicomtestapp.ui.main.ResultsFragment
import com.baarton.kiwicomtestapp.ui.main.StartFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StartFragment.newInstance())
                    .commitNow()
        }
    }
}