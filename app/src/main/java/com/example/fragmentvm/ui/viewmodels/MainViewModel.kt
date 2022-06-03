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
import com.example.fragmentvm.core.utils.NetworkResult
import com.example.fragmentvm.data.datasource.CatPagingSource
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.StateVote
import com.example.fragmentvm.ui.utils.VotesEnum
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainViewModel : ViewModel() {
    private val _stateUIMain = MutableStateFlow<StateMain>(StateMain.Empty)
    fun getStateUIMain(): StateFlow<StateMain> = _stateUIMain

    private val _stateUIVote = MutableStateFlow<StateVote<VoteResponseDomain>>(StateVote.Empty)
    fun getStateUIVote(): StateFlow<StateVote<VoteResponseDomain>> = _stateUIVote

    private var _exceptionMessage = MutableLiveData<String>()
    val exceptionMessage: LiveData<String> get() = _exceptionMessage

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    val catsFlow = Pager(PagingConfig(pageSize = 10, initialLoadSize = 10)) { CatPagingSource() }
        .flow
        .cachedIn(viewModelScope)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    private fun onException(throwable: Throwable) {
        Timber.e(throwable)
        _exceptionMessage.postValue(throwable.message)
    }

    @Inject
    lateinit var repository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface

    fun vote(catModel: CatDomain, vote: VotesEnum, position: Int) {
        _stateUIVote.value = StateVote.Loading
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            when (
                val response =
                    repository.postVote(apikey, VoteRequestDomain(catModel.id, vote))
            ) {
                is NetworkResult.Error -> {
                    val payload = VoteResponseDomain(
                        position = position,
                        vote = vote,
                        message = response.message.toString(),
                    )
                    _stateUIVote.value = StateVote.UnSuccess(payload)
                }
                is NetworkResult.Exception -> {
                    _stateUIVote.value = StateVote.Error(response.e)
                    Timber.e(response.e)
                }
                is NetworkResult.Success -> {
                    val payload = response.data.copy(position = position, vote = vote)
                    _stateUIVote.value = StateVote.Success(payload)
                }
            }
        }
    }

    fun setState(state: StateMain) {
        _stateUIMain.value = state
    }

    fun resetVoteState() {
        _stateUIVote.value = StateVote.Empty
    }
}
