package com.example.fragmentvm.utils

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object Util {
    private const val TIMEOUT_KEYBOARD: Long = 50
    private const val EMAIL_REGEX = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"

    fun isEmailValid(email: String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email)
    }

    fun isNotEmpty(description: String): Boolean {
        return description.isNotEmpty()
    }

    fun <Left, Right, Result> combine(
        leftLiveData: LiveData<Left>,
        rightLiveData: LiveData<Right>,
        merge: (leftValue: Left, rightValue: Right) -> Result,
    ): LiveData<Result> {
        val mediator = MediatorLiveData<Result>()

        fun combineLatestData() {
            val leftValue = leftLiveData.value ?: return
            val rightValue = rightLiveData.value ?: return
            val result = merge(leftValue, rightValue)
            mediator.value = result!!
        }

        mediator.addSource(leftLiveData) { combineLatestData() }
        mediator.addSource(rightLiveData) { combineLatestData() }

        return mediator
    }

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

    fun toObservable(editText: TextInputEditText): Observable<String> {
        val observable = Observable.create<String> { emitter ->
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    s?.toString()?.let { emitter.onNext(it) }
                }

                override fun afterTextChanged(p0: Editable?) {}
            }
            editText.addTextChangedListener(textWatcher)
            emitter.setCancellable {
                editText.removeTextChangedListener(textWatcher)
            }
        }

        return observable.debounce(TIMEOUT_KEYBOARD, TimeUnit.MILLISECONDS)
    }
}
