package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.core.utils.SingleLiveEvent
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.core.utils.Util
import com.example.fragmentvm.data.model.favourite.post.FavoriteRequestMapper
import com.example.fragmentvm.data.model.favourite.post.FavoriteResponseMapper
import com.example.fragmentvm.data.model.favourite.post.FavouriteResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.BackendResponseDomain
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteRequestDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.ui.utils.StateVote
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _stateUI = MutableStateFlow<StateVote<BackendResponseDomain>>(StateVote.Empty)
    fun getStateUI(): StateFlow<StateVote<BackendResponseDomain>> = _stateUI

    private val _stateFulData =
        MutableStateFlow<StatefulData<BackendResponseDomain>>(StatefulData.Loading)

    fun getStatefulData(): StateFlow<StatefulData<BackendResponseDomain>> = _stateFulData

    private val _test =
        MutableStateFlow<StatefulData<BackendResponseDomain>>(StatefulData.Loading)
    val test: MutableStateFlow<StatefulData<BackendResponseDomain>>
        get() = _test

    private val _catModel = SingleLiveEvent<CatDomain>()
    fun getCat(): LiveData<CatDomain> = _catModel
    fun setCat(item: CatDomain) {
        _catModel.value = item
    }

    fun setFavourite(cat: CatDomain) {
        viewModelScope.launch(Dispatchers.IO) {
            val favouriteRequest = FavoriteRequestMapper()
                .mapFromDomainModel(
                    FavouriteRequestDomain(
                        cat.id,
                        "demo-440b14"
                    )
                )
            catRepository.postFavourite(apikey, favouriteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let { body: FavouriteResponseDto ->
                            val favouriteResponse: FavouriteResponseDomain =
                                FavoriteResponseMapper().mapToDomainModel(body)
                        }
                    } else {
                        it.errorBody()?.let { body: ResponseBody ->
                            try {
                                val error = Util.parseBackendResponseError(body)
                                val response = BackendResponseDtoMapper().mapToDomainModel(error)
                                Timber.d("Ошибка при добавление в избранное ${response.message}")
                                _test.value = StatefulData.Error(response.message)
                                _stateUI.value = StateVote.UnSuccess(response)
                                _stateFulData.value = StatefulData.Error(response.message)
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }
                    }
                }, {
                    _stateUI.value = StateVote.Error(it)
                    Timber.e(it)
                })
        }
    }
}