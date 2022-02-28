package com.example.fragmentvm.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.model.backend.BackendResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit

object Util {
    private const val TIMEOUT_KEYBOARD: Long = 50

    fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

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

    fun parseResponseError(errorBody: ResponseBody?): BackendResponse {
        val gson = Gson()
        val adapter: TypeAdapter<BackendResponse> =
            gson.getAdapter(BackendResponse::class.java)
        return adapter.fromJson(errorBody?.string())
    }
}
