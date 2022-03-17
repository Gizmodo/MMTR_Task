package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants.DataStore.KEY_FLAGREG
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.domain.DataStoreInterface
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {
    @Inject
    lateinit var dataStore: DataStoreInterface

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