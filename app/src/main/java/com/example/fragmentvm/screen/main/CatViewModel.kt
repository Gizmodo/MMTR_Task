package com.example.fragmentvm.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.model.cat.CatModel
import com.example.fragmentvm.utils.SingleLiveEvent

class CatViewModel : ViewModel() {
    private val _catModel = SingleLiveEvent<CatModel>()
    fun getCat(): LiveData<CatModel> = _catModel
    fun setCat(item: CatModel) {
        _catModel.value = item
    }
}