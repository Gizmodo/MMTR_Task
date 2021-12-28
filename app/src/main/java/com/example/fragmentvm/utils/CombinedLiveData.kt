package com.example.fragmentvm.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombinedLiveData {
    companion object {
        fun <Left, Right, Result> combine(
            leftLiveData: LiveData<Left>,
            rightLiveData: LiveData<Right>,
            merge: (leftValue: Left, rightValue: Right) -> Result,
        ): LiveData<Result> {

            val mediator = MediatorLiveData<Result>()

            fun combineLatestData() {
                val leftValue = leftLiveData.value ?: return
                val rightValue = rightLiveData.value ?: return
                val result =merge(leftValue, rightValue)
                mediator.value = result!!
            }

            mediator.addSource(leftLiveData) { combineLatestData() }
            mediator.addSource(rightLiveData) { combineLatestData() }

            return mediator
        }
    }
}