package com.example.fragmentvm

import androidx.lifecycle.ViewModel
import ru.mmtr.data.api.network.data.DataStoreRepository
import kotlinx.coroutines.runBlocking
import ru.mmtr.domain.utils.SingleLiveEvent
import ru.mmtr.domain.utils.Util.FLAGREG
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