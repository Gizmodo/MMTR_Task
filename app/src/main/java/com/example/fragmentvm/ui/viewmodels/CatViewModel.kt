package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.*
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

    fun setFavourite(cat: CatDomain, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteRequest = FavoriteRequestMapper()
                .mapFromDomainModel(FavouriteRequestDomain(cat.id, "demo-440b14"))
            catRepository.postFavourite(apikey, favouriteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _favouriteState.value = StatefulData.Loading
                    if (it.isSuccessful) {
                        it.body()?.let { body: FavouriteResponseDto ->
                            val favouriteResponse: FavouriteResponseDomain =
                                FavoriteResponseMapper().mapToDomainModel(body)
                            favouriteResponse.position = position
                            favouriteState.value = StatefulData.Success(favouriteResponse)
                        }
                    } else {
                        it.errorBody()?.let { body: ResponseBody ->
                            try {
                                val error = Util.parseBackendResponseError(body)
                                val response = BackendResponseDtoMapper().mapToDomainModel(error)
                                Timber.d("Ошибка при добавление в избранное: ${response.message}")
                                _favouriteState.value =
                                    StatefulData.ErrorUiText(UiText.StringResource(
                                        resId = R.string.favourite_error,
                                        response.message
                                    ))
                                // TODO: Попробовать снять лайк по ID ранее установленного лайка
                                cat.idFavourite?.let {
                                    Timber.d("Есть возможность удалить избранное $cat.idFavourite")
                                    /*
                                         catRepository.deleteFavourite(apikey, cat.idFavourite!!)
                                             .observeOn(AndroidSchedulers.mainThread())
                                             .subscribe({

                                             },{

                                             })
                                         */
                                }

                            } catch (e: Exception) {
                                Timber.e("Возникло исключение: $e")
                                _favouriteState.value =
                                    StatefulData.ErrorUiText(UiText.StringResource(
                                        resId = R.string.exception,
                                        e.message.toString()
                                    ))
                            }
                        }
                    }
                }, {
                    _favouriteState.value =
                        StatefulData.ErrorUiText(UiText.StringResource(
                            resId = R.string.error_subscribe,
                            it.message.toString()
                        ))
                    Timber.e(it)
                })
        }
    }
}