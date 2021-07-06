package com.baarton.kiwicomtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baarton.kiwicomtestapp.ui.StartFragment

//TODO don't forget:
// > tests
// > extractions - refactoring - deleting stuff where possible
// > annotations? UiThread?
// > exceptions handling and logging at least
// > layout refresh, cleanup + styles extractions
// > permission checks? network connectivity?
// > let db be asynchronous
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