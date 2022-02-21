package com.example.fragmentvm.screen

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.data.DataStoreRepository
import com.example.fragmentvm.utils.Constants.DataStore.KEY_FLAGREG
import com.example.fragmentvm.utils.SingleLiveEvent
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
        dataStore.getBool(KEY_FLAGREG).let {
            _isAlreadyRegistered.postValue(it)
        }
    }
}