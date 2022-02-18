package com.example.fragmentvm.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.mmtr.domain.model.Cat
import ru.mmtr.domain.utils.SingleLiveEvent

class CatViewModel : ViewModel() {
    private val _cat = SingleLiveEvent<Cat>()
    fun getCat(): LiveData<Cat> = _cat
    fun setCat(item: Cat) {
        _cat.value = item
    }
}