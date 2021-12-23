package com.example.fragmentvm.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class Util {
    companion object {
        fun isEmailValid(email: String): Boolean {
            val regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun <T> LiveData<T>.skipFirst(): MutableLiveData<T> {
            val result = MediatorLiveData<T>()
            var isFirst = true
            result.addSource(this) {
                if (isFirst) isFirst = false
                else result.value = it
            }
            return result
        }
    }
}