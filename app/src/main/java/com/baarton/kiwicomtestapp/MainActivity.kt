package com.baarton.kiwicomtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baarton.kiwicomtestapp.ui.StartFragment


//TODO don't forget:
// > tests?
// > permission checks? network connectivity?
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