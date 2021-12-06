package com.example.fragmentvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fragmentvm.network.MainRepository
import com.example.fragmentvm.ui.second.MainViewModelTest
import timber.log.Timber

class MyViewModelFactory constructor(protected val repository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModelTest::class.java)) {
            MainViewModelTest(this.repository) as T
        } else {
            Timber.e("ViewModel Not Found")
            throw  IllegalArgumentException("ViewModel Not Found")
        }
    }
}