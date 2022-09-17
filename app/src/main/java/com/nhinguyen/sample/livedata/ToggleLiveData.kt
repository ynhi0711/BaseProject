package com.nhinguyen.sample.livedata

import androidx.lifecycle.MediatorLiveData

class ToggleLiveData : MediatorLiveData<Void>() {
    fun toggle() {
        postValue(value)
    }
}