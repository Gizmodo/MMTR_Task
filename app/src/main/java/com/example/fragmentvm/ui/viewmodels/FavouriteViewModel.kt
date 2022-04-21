package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.data.datasource.FavCatPagingSource
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.ui.utils.StateMain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FavouriteViewModel : ViewModel() {
    private val _stateUIMain = MutableStateFlow<StateMain>(StateMain.Empty)
    fun getStateUIMain(): StateFlow<StateMain> = _stateUIMain

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _favouriteImagesLiveData = MutableLiveData<FavCatDomain>()
    val favouriteImagesLiveData: LiveData<FavCatDomain>
        get() = _favouriteImagesLiveData

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    @Inject
    lateinit var repository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface
    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    val catsFavFlow = Pager(PagingConfig(pageSize = 10, initialLoadSize = 10))
    { FavCatPagingSource() }
        .flow
        .cachedIn(viewModelScope)
    fun setState(state: StateMain) {
        _stateUIMain.value = state
    }
    /*private fun loadUsers() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = settingsApi.getUsersSuspend()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _usersLiveData.postValue(response.body())
                    _loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }*/

    private fun onError(message: String) {
        _errorMessage.postValue(message)
        _loading.postValue(true)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun onFavClicked(favCat: FavCatDomain, position: Int) {
        TODO("Not yet implemented")
    }
}