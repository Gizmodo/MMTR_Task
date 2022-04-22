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
import com.example.fragmentvm.core.utils.*
import com.example.fragmentvm.data.datasource.FavCatPagingSource
import com.example.fragmentvm.data.model.favourite.delete.FavouriteResponseDeleteDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.model.vote.request.VoteRequestMapper
import com.example.fragmentvm.data.model.vote.response.VoteResponseMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.VotesEnum
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class FavouriteViewModel : ViewModel() {
    /***
     * States
     * ***/
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

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    /*private var _favouriteImagesLiveData = MutableLiveData<FavCatDomain>()
    val favouriteImagesLiveData: LiveData<FavCatDomain>
        get() = _favouriteImagesLiveData*/

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    @Inject
    lateinit var catRepository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface
    var job: Job? = null


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

    fun onFavClicked(favCat: FavCatDomain, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            catRepository.deleteFavourite(apikey, favCat.id)
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
                                                adapterPosition = position
                                            )
                                        _favState.value =
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
                                                    bodyOnDelete)
                                            val resOnDelete =
                                                BackendResponseDtoMapper().mapToDomainModel(
                                                    errorOnDelete)
                                            Timber.d("Ошибка при удалении из избранного: ${resOnDelete.message}")
                                            _favState.value =
                                                StatefulData.ErrorUiText(
                                                    UiText.StringResource(
                                                        R.string.favourite_error_remove,
                                                        resOnDelete.message
                                                    ))
                                        } catch (e: Exception) {
                                            handleExceptionNew(e,_favState)
                                        }
                                    }
                            }
                        }
                    } catch (e: Exception) {
                        handleExceptionNew(e,_favState)
                    }
                }, { _throw ->
                    handleFavThrow(_throw, _favState)
                })
        }
    }

    fun vote(cat: FavCatDomain, vote: VotesEnum, position: Int) {
        Timber.d("Vote clicked ${vote.name} $position")
        // TODO: Сделать голосовалку
        viewModelScope.launch(Dispatchers.IO) {
            val voteRequest = VoteRequestMapper()
                .mapFromDomainModel(VoteRequestDomain(cat.imageId, vote))
            catRepository.postVote(apikey, voteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let { body ->
                            val voteResponse = VoteResponseMapper()
                                .mapToDomainModel(body)
                            voteResponse.position = position
                            voteResponse.vote = vote
                            _voteState.value = StatefulData.Success(voteResponse)
                        }
                    } else {
                        it.errorBody()?.let { body ->
                            try {
                                val error = Util.parseBackendResponseError(body)
                                val parsed = BackendResponseDtoMapper().mapToDomainModel(error)
                                val voteResponse = VoteResponseDomain(
                                    position = position,
                                    vote = vote,
                                    message = parsed.message,
                                )
                                _voteState.value = StatefulData.ErrorUiText(
                                    UiText.StringResource(
                                        resId = R.string.vote_error_add,
                                        parsed.message
                                    ))
                            } catch (e: Exception) {
                                // DONE: сменить на свой собственный Handle
                                handleExceptionNew(e,_voteState)

                            }
                        }
                    }
                }, { _throw ->
                    handleVoteThrow(_throw, _voteState)
                })

        }
    }

    fun showCat(favCat: FavCatDomain) {
// TODO: Отобразить кота при клике по нему
    }

    private fun handleVoteThrow(
        _throw: Throwable,
        stateHolder: MutableStateFlow<StatefulData<VoteResponseDomain>>,
    ) {
        stateHolder.value =
            StatefulData.ErrorUiText(UiText.StringResource(
                resId = R.string.error_subscribe,
                _throw.message.toString()
            ))
        Timber.e(_throw)
    }

    private fun handleFavThrow(
        _throw: Throwable,
        stateHolder: MutableStateFlow<StatefulData<FavouriteResponseDomain>>,
    ) {
        stateHolder.value =
            StatefulData.ErrorUiText(UiText.StringResource(
                resId = R.string.error_subscribe,
                _throw.message.toString()
            ))
        Timber.e(_throw)
    }

    private inline fun <reified T : Any> handleExceptionNew(
        e: Exception,
        stateHolder: MutableStateFlow<StatefulData<T>>,
    ) {
        Timber.e("Возникло исключение: $e")
        if (Generic<T>().checkType(FavouriteResponseDomain(1, "", 1))) {
            // TODO: Может и не нужен T  ???
            stateHolder.value =
                StatefulData.ErrorUiText(UiText.StringResource(
                    R.string.exception,
                    e.message.toString()
                ))
        }
        if (Generic<T>().checkType(VoteResponseDomain(-1, "", 0, VotesEnum.DOWN))) {
            stateHolder.value =
                StatefulData.ErrorUiText(UiText.StringResource(
                     R.string.exception,
                    e.message.toString()
                ))
        }
    }

    private fun handleException(e: Exception) {
        Timber.e("Возникло исключение: $e")
        _favState.value =
            StatefulData.ErrorUiText(UiText.StringResource(
                resId = R.string.exception,
                e.message.toString()
            ))
    }

    private fun onError(message: String) {
        _errorMessage.postValue(message)
        _loading.postValue(true)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}