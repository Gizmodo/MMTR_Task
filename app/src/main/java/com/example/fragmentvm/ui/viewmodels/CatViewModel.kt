package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.*
import com.example.fragmentvm.data.model.favourite.delete.FavouriteResponseDeleteDto
import com.example.fragmentvm.data.model.favourite.post.FavoriteRequestMapper
import com.example.fragmentvm.data.model.favourite.post.FavoriteResponseMapper
import com.example.fragmentvm.data.model.favourite.post.FavouriteResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteRequestDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
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

    private val _favouriteState =
        MutableStateFlow<StatefulData<FavouriteResponseDomain>>(StatefulData.Loading)
    val favouriteState: MutableStateFlow<StatefulData<FavouriteResponseDomain>>
        get() = _favouriteState

    private val _catModel = SingleLiveEvent<CatDomain>()
    fun getCat(): LiveData<CatDomain> = _catModel
    fun setCat(item: CatDomain) {
        _catModel.value = item
    }

    fun resetFavouriteState() {
        _favouriteState.value = StatefulData.Loading
    }

    fun setFavourite(cat: CatDomain, adapterPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteRequest = FavoriteRequestMapper()
                .mapFromDomainModel(FavouriteRequestDomain(cat.id))
            catRepository.postFavourite(apikey, favouriteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _favouriteState.value = StatefulData.Loading
                    if (it.isSuccessful) {
                        it.body()?.let { body: FavouriteResponseDto ->
                            val favouriteResponse: FavouriteResponseDomain =
                                FavoriteResponseMapper().mapToDomainModel(body)
                            favouriteResponse.adapterPosition = adapterPosition
                            favouriteState.value = StatefulData.Success(favouriteResponse)
                        }
                    } else {
                        it.errorBody()?.let { body: ResponseBody ->
                            try {
                                val error = Util.parseBackendResponseError(body)
                                val response = BackendResponseDtoMapper().mapToDomainModel(error)
                                when (cat.idFavourite == null) {
                                    true -> {
                                        Timber.d("Ошибка при добавление в избранное: ${response.message}")
                                        _favouriteState.value =
                                            StatefulData.ErrorUiText(
                                                UiText.StringResource(
                                                    resId = R.string.favourite_error_add,
                                                    response.message
                                                )
                                            )
                                    }
                                    false -> {
                                        cat.idFavourite?.let {
                                            catRepository.deleteFavourite(apikey, cat.idFavourite!!)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe({ responseOnDelete ->
                                                    try {
                                                        when {
                                                            responseOnDelete.isSuccessful -> {
                                                                responseOnDelete.body()
                                                                    ?.let { body: FavouriteResponseDeleteDto ->
                                                                        val favouriteResponse =
                                                                            FavouriteResponseDomain(
                                                                                id = null,
                                                                                message = body.message,
                                                                                adapterPosition = adapterPosition
                                                                            )
                                                                        favouriteState.value =
                                                                            StatefulData.Success(
                                                                                favouriteResponse
                                                                            )
                                                                    }
                                                            }
                                                            else -> {
                                                                responseOnDelete.errorBody()
                                                                    ?.let { bodyOnDelete: ResponseBody ->
                                                                        try {
                                                                            val errorOnDelete =
                                                                                Util.parseBackendResponseError(
                                                                                    bodyOnDelete
                                                                                )
                                                                            val resOnDelete =
                                                                                BackendResponseDtoMapper().mapToDomainModel(
                                                                                    errorOnDelete
                                                                                )
                                                                            Timber.d("Ошибка при удалении из избранного: ${resOnDelete.message}")
                                                                            _favouriteState.value =
                                                                                StatefulData.ErrorUiText(
                                                                                    UiText.StringResource(
                                                                                        resId = R.string.favourite_error_remove,
                                                                                        resOnDelete.message
                                                                                    )
                                                                                )
                                                                        } catch (e: Exception) {
                                                                            handleException(e)
                                                                        }
                                                                    }
                                                            }
                                                        }
                                                    } catch (e: Exception) {
                                                        handleException(e)
                                                    }
                                                }, { _throw ->
                                                    handleObservableThrow(_throw)
                                                })
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                handleException(e)
                            }
                        }
                    }
                }, {
                    handleObservableThrow(it)
                })
        }
    }

    private fun handleObservableThrow(_throw: Throwable) {
        _favouriteState.value =
            StatefulData.ErrorUiText(
                UiText.StringResource(
                    resId = R.string.error_subscribe,
                    _throw.message.toString()
                )
            )
        Timber.e(_throw)
    }

    private fun handleException(e: Exception) {
        Timber.e("Возникло исключение: $e")
        _favouriteState.value =
            StatefulData.ErrorUiText(
                UiText.StringResource(
                    resId = R.string.exception,
                    e.message.toString()
                )
            )
    }
}