package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.repository.DataStoreRepositoryImpl
import com.example.fragmentvm.repository.RepositoryRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<CatUiState>(CatUiState.Empty)
    val uiState: StateFlow<CatUiState> = _uiState

    init {
        App.instance().appGraph.embed(this)
        getCats()
    }

    @Inject
    lateinit var repositoryRetrofit: RepositoryRetrofit

    @Inject
    lateinit var ds: DataStoreRepositoryImpl

    private var _cats = MutableLiveData<List<Cat>>()
    val cats: LiveData<List<Cat>>
        get() = _cats

    fun getCats() {
        _uiState.value = CatUiState.Loading
        val apikey = runBlocking { ds.getString("apikey") }
        viewModelScope.launch(Dispatchers.IO) {
            repositoryRetrofit.getCats(apikey.toString())
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            _uiState.value = CatUiState.Empty
                        } else {
                            _uiState.value = CatUiState.Finished
                            _cats.postValue(it)
                        }
                    },
                    {
                        Timber.e(it)
                        _uiState.value = CatUiState.Error(it)
                    })
        }
    }

    sealed class CatUiState {
        object Empty : CatUiState()
        object Loading : CatUiState()
        object Finished : CatUiState()
        class Loaded(val data: List<Cat>) : CatUiState()
        class Error(val t: Throwable) : CatUiState()
    }
}