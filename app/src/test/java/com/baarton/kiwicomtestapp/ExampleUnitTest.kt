package com.baarton.kiwicomtestapp

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.withFragment
import com.baarton.kiwicomtestapp.app.TestApp
import com.baarton.kiwicomtestapp.ui.results.ResultsFragment
import com.baarton.kiwicomtestapp.ui.results.ResultsViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(application = TestApp::class, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest : KoinTest {

    @Test
    fun initViewModelTest() {

        val fr = launchFragment {
            ResultsFragment.newInstance()
        }

        fr.withFragment {
            val model = ResultsViewModel(this)
            assertEquals("NO RESULTS", model.infoText.value)
            //TODO etc.
        }

    }

    //TODO other tests

}