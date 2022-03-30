package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.domain.model.cat.CatDomain

class CatViewModel : ViewModel() {
    private val _catModel = SingleLiveEvent<CatDomain>()
    fun getCat(): LiveData<CatDomain> = _catModel
    fun setCat(item: CatDomain) {
        _catModel.value = item
    }
}