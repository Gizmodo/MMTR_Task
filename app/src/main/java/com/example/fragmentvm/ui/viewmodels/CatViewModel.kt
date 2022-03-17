package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.model.cat.CatModel

class CatViewModel : ViewModel() {
    private val _catModel = SingleLiveEvent<CatModel>()
    fun getCat(): LiveData<CatModel> = _catModel
    fun setCat(item: CatModel) {
        _catModel.value = item
    }
}