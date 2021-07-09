package com.baarton.kiwicomtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baarton.kiwicomtestapp.ui.StartFragment

//TODO don't forget:
// > utilize VM more
// > extractions - refactoring - deleting stuff where possible
// > layout refresh, cleanup + styles extractions
// > tests
// > exceptions handling and logging at least - check that everywhere
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