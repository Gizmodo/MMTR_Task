package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.core.utils.NetworkResult
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.core.utils.UiText
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteRequestDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class CatViewModel : ViewModel() {
    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    @Inject
    lateinit var ds: DataStoreInterface

    @Inject
    lateinit var catRepository: CatRepository
    private var _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage: LiveData<String> get() = _exceptionMessage

    private val _favouriteState =
        MutableStateFlow<StatefulData<FavouriteResponseDomain>>(StatefulData.Loading)
    val favouriteState: MutableStateFlow<StatefulData<FavouriteResponseDomain>>
        get() = _favouriteState
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    private fun onException(throwable: Throwable) {
        Timber.e(throwable)
        _exceptionMessage.postValue(throwable.message)
    }

    private val _catModel = SingleLiveEvent<CatDomain>()
    fun getCat(): LiveData<CatDomain> = _catModel
    fun setCat(item: CatDomain) {
        _catModel.value = item
    }

    fun resetFavouriteState() {
        _favouriteState.value = StatefulData.Loading
    }

    fun setFavourite(cat: CatDomain, adapterPosition: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _favouriteState.value = StatefulData.Loading
            when (val response = catRepository.postFavourite(
                apikey, FavouriteRequestDomain(cat.id)
            )) {
                is NetworkResult.Error -> {
                    when (cat.idFavourite == null) {
                        true -> {
                            Timber.d("Не удалось добавить в избранное: ${response.message}")
                            _favouriteState.value =
                                StatefulData.ErrorUiText(
                                    UiText.StringResource(
                                        resId = R.string.favourite_error_add,
                                        response.message.toString()
                                    )
                                )
                        }
                        false -> {
                            cat.idFavourite?.let {
                                when (val removeResponse = catRepository.deleteFavourite(
                                    apikey,
                                    cat.idFavourite!!
                                )) {
                                    is NetworkResult.Error -> {
                                        Timber.d("Ошибка при удалении из избранного: ${removeResponse.message}")
                                        _favouriteState.value =
                                            StatefulData.ErrorUiText(
                                                UiText.StringResource(
                                                    resId = R.string.favourite_error_remove,
                                                    removeResponse.message.toString()
                                                )
                                            )
                                    }
                                    is NetworkResult.Exception -> {
                                        Timber.e(removeResponse.e)
                                    }
                                    is NetworkResult.Success -> {
                                        val favouriteResponse =
                                            FavouriteResponseDomain(
                                                id = null,
                                                message = removeResponse.data.message,
                                                adapterPosition = adapterPosition
                                            )
                                        _favouriteState.value =
                                            StatefulData.Success(favouriteResponse)
                                    }
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Exception -> {
                    Timber.e(response.e)
                }
                is NetworkResult.Success -> {
                    val payload: FavouriteResponseDomain =
                        response.data.copy(adapterPosition = adapterPosition)
                    _favouriteState.value = StatefulData.Success(payload)
                }
            }
        }
    }
}
