package com.example.fragmentvm.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel() : ViewModel() {
    var urlCat: MutableLiveData<String?> = MutableLiveData<String?>().apply { value = "" }

}