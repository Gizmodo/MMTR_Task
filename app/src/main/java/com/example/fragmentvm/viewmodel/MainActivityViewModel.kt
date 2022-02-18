package com.example.fragmentvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.repository.data.DataStoreRepository
import com.example.fragmentvm.utils.SingleLiveEvent
import com.example.fragmentvm.utils.Util.FLAGREG
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {
    @Inject
    lateinit var dataStore: DataStoreRepository

    private var _isAlreadyRegistered = SingleLiveEvent<Boolean>()
    fun getIsAlreadyRegistered(): SingleLiveEvent<Boolean> = _isAlreadyRegistered

    init {
        App.instance().appGraph.embed(this)
        getFlag()
    }

    private fun getFlag() = runBlocking {
        dataStore.getBool(FLAGREG).let {
            _isAlreadyRegistered.postValue(it)
        }
    }
}