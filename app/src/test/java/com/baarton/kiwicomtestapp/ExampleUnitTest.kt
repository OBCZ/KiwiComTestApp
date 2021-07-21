package com.baarton.kiwicomtestapp

import android.os.Build
import android.view.View
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
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
        val model = ResultsViewModel()
        assertEquals(R.string.text_results_nothing, model.infoTextRes.value)
        assertEquals(0, model.overviewTextItemAmount.value)
        assertEquals(View.GONE, model.overviewTextVisibility.value)
        assertEquals(View.VISIBLE, model.infoTextVisibility.value)
        assertEquals(View.GONE, model.progressBarVisibility.value)
    }

    @Test
    fun resultsFragmentTest() {
        val fragmentUnderTest = launchFragment { ResultsFragment.newInstance() }
        fragmentUnderTest.moveToState(Lifecycle.State.CREATED).onFragment {
            //TODO actual test
        }
    }

}