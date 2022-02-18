package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.SingleLiveEvent

class CatViewModel : ViewModel() {
    private val _cat = SingleLiveEvent<Cat>()
    fun getCat(): LiveData<Cat> = _cat
    fun setCat(item: Cat) {
        _cat.value = item
    }
}