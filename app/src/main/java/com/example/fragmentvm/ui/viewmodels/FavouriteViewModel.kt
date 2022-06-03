package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.fragmentvm.App
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.core.utils.NetworkResult
import com.example.fragmentvm.core.utils.StatefulData
import com.example.fragmentvm.core.utils.UiText
import com.example.fragmentvm.data.datasource.FavCatPagingSource
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.VotesEnum
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class FavouriteViewModel : ViewModel() {
    private val _stateUIMain = MutableStateFlow<StateMain>(StateMain.Empty)
    fun getStateUIMain(): StateFlow<StateMain> = _stateUIMain

    private val _favState =
        MutableStateFlow<StatefulData<FavouriteResponseDomain>>(StatefulData.Loading)
    val favState: StateFlow<StatefulData<FavouriteResponseDomain>>
        get() = _favState

    private val _voteState =
        MutableStateFlow<StatefulData<VoteResponseDomain>>(StatefulData.Loading)
    val voteState: StateFlow<StatefulData<VoteResponseDomain>>
        get() = _voteState

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val eventChannel = Channel<StatefulData<FavCatDomain>>()
    val evenFlow = eventChannel.receiveAsFlow()

    fun setCat(cat: FavCatDomain) {
        viewModelScope.launch {
            when {
                cat.image_url != null -> {
                    eventChannel.send(StatefulData.Success(cat))
                }
                else -> {
                    eventChannel.send(
                        StatefulData.ErrorUiText(
                            UiText.StringResource(R.string.favourite_error_set_cat, cat.imageId)
                        )
                    )
                }
            }
        }
    }

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    @Inject
    lateinit var catRepository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface

    private var _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage: LiveData<String> get() = _exceptionMessage

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    private fun onException(throwable: Throwable) {
        Timber.e(throwable)
        _exceptionMessage.postValue(throwable.message)
    }

    val catsFavFlow = Pager(PagingConfig(pageSize = 10, initialLoadSize = 10)) {
        FavCatPagingSource()
    }
        .flow
        .cachedIn(viewModelScope)

    fun setState(state: StateMain) {
        _stateUIMain.value = state
    }

    fun onFavClicked(favCat: FavCatDomain, position: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            when (val response = catRepository.deleteFavourite(apikey, favCat.id)) {
                is NetworkResult.Error -> {
                    Timber.d("Ошибка при удалении из избранного: ${response.message}")
                    _favState.value =
                        StatefulData.ErrorUiText(
                            UiText.StringResource(
                                resId = R.string.favourite_error_remove,
                                response.message.toString()
                            )
                        )
                }
                is NetworkResult.Exception -> {
                    Timber.e(response.e)
                }
                is NetworkResult.Success -> {
                    val favouriteResponse =
                        FavouriteResponseDomain(
                            id = null,
                            message = response.data.message,
                            adapterPosition = position
                        )
                    _favState.value = StatefulData.Success(favouriteResponse)
                }
            }
        }
    }

    fun vote(cat: FavCatDomain, vote: VotesEnum, position: Int) {
        Timber.d("Vote clicked ${vote.name} $position")
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            when (
                val response =
                    catRepository.postVote(apikey, VoteRequestDomain(cat.imageId, vote))
            ) {
                is NetworkResult.Error -> {
                    _voteState.value = StatefulData.ErrorUiText(
                        UiText.StringResource(
                            resId = R.string.vote_error_add,
                            response.message.toString()
                        )
                    )
                }
                is NetworkResult.Exception -> {
                    Timber.e(response.e)
                }
                is NetworkResult.Success -> {
                    val payload = response.data.copy(position = position, vote = vote)
                    _voteState.value = StatefulData.Success(payload)
                }
            }
        }
    }

    fun resetState() {
        _voteState.value = StatefulData.Loading
    }
}
