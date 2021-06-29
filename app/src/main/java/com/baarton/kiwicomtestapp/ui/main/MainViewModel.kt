package com.baarton.kiwicomtestapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    internal val liveData = MutableLiveData<String>()

    init {
        liveData.value = "NO DATA LOADED"
    }


}