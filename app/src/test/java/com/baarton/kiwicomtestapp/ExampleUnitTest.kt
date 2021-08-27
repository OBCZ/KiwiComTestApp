package com.baarton.kiwicomtestapp

import android.os.Build
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.baarton.kiwicomtestapp.app.TestApp
import com.baarton.kiwicomtestapp.ui.results.ResultsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(application = TestApp::class, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest : KoinTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun initViewModelTest() {
        val model = ResultsViewModel()
        assertEquals(R.string.text_results_nothing, model.infoTextRes.value)
        assertEquals(0, model.overviewTextItemAmount.value)
        assertEquals(View.GONE, model.overviewTextVisibility.value)
        assertEquals(View.VISIBLE, model.infoTextVisibility.value)
        assertEquals(View.GONE, model.progressBarVisibility.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadDataTest() {
        testCoroutineRule.runBlockingTest {
            val model = ResultsViewModel()
            model.loadData()
        }
        //TODO actual test
        assert(true)
    }

}