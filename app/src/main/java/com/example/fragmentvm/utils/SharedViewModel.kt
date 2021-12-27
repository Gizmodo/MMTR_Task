package com.example.fragmentvm.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.Cat

class SharedViewModel : ViewModel() {
    val selectedCat = MutableLiveData<Cat>()
    fun select(cat: Cat) {
        selectedCat.value = cat
    }
}