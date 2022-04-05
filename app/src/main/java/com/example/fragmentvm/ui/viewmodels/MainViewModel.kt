package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.core.utils.Util
import com.example.fragmentvm.data.CatPagingSource
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.model.vote.request.VoteRequestMapper
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.StateVote
import com.example.fragmentvm.ui.utils.VotesEnum
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class MainViewModel : ViewModel() {

    private val _stateUIMain = MutableStateFlow<StateMain>(StateMain.Empty)
    fun getStateUIMain(): StateFlow<StateMain> = _stateUIMain

    private val _stateUIVote = MutableStateFlow<StateVote<VoteResponseDomain>>(StateVote.Empty)
    fun getStateUIVote(): StateFlow<StateVote<VoteResponseDomain>> = _stateUIVote

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
//              getCats()
    }

    val cats: Flow<PagingData<CatDomain>> = Pager(PagingConfig(10))
    { CatPagingSource() }.flow
        .cachedIn(viewModelScope)

    @Inject
    lateinit var repository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface

    fun vote(catModel: CatDomain, vote: VotesEnum, position: Int) {
        _stateUIVote.value = StateVote.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val voteRequest = VoteRequestMapper()
                .mapFromDomainModel(
                    VoteRequestDomain(catModel.id, vote)
                )
            repository.postVote(apikey, voteRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let { body: VoteResponseDto ->
                            val voteResponse: VoteResponseDomain =
                                VoteResponseMapper().mapToDomainModel(body)
                            voteResponse.position = position
                            voteResponse.vote = vote
                            _stateUIVote.value = StateVote.Success(voteResponse)
                        }
                    } else {
                        it.errorBody()?.let { body: ResponseBody ->
                            Timber.d("Body response not null")
                            try {
                                val error: BackendResponseDto = Util.parseBackendResponseError(body)
                                val parsed = BackendResponseDtoMapper().mapToDomainModel(error)
                                val voteResponse = VoteResponseDomain(
                                    position = position,
                                    vote = vote,
                                    message = parsed.message,
                                )
                                _stateUIVote.value = StateVote.UnSuccess(voteResponse)
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }
                    }
                }, {
                    _stateUIVote.value = StateVote.Error(it)
                    Timber.e(it)
                })
        }
    }

    fun setState(state: StateMain) {
        _stateUIMain.value = state
    }

    fun resetVoteState() {
        _stateUIVote.value = StateVote.Empty
    }
}