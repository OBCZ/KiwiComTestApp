package com.baarton.kiwicomtestapp.ui.results

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ResultsViewModel : ViewModel() {

    internal val infoText = MutableLiveData<String>()
    internal val overviewText = MutableLiveData<String>()
    internal val infoTextVisibility = MutableLiveData<Int>()
    internal val progressBarVisibility = MutableLiveData<Int>()

    init {
        overviewText.value = "NO RESULTS"
        infoText.value = "NO DATA LOADED"
        infoTextVisibility.value = View.VISIBLE
        progressBarVisibility.value = View.GONE
    }

}