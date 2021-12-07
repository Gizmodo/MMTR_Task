package com.example.fragmentvm.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentvm.repository.MainRepository
import com.example.fragmentvm.ui.second.SecondViewModel
import timber.log.Timber

class MyViewModelFactory constructor(protected val repository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SecondViewModel::class.java)) {
            SecondViewModel(this.repository) as T
        } else {
            Timber.e("ViewModel Not Found")
            throw  IllegalArgumentException("ViewModel Not Found")
        }
    }
}