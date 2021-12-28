package com.example.fragmentvm.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class Util {
    companion object {
        fun <T> LiveData<T>.skipFirst(): MutableLiveData<T> {
            val result = MediatorLiveData<T>()
            var isFirst = true
            result.addSource(this) {
                when (isFirst) {
                    true -> isFirst = false
                    false -> result.value = it
                }
            }
            return result
        }
    }
}