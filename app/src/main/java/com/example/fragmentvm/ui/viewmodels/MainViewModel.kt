package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.core.utils.Util
import com.example.fragmentvm.data.RetrofitRepository
import com.example.fragmentvm.data.model.cat.CatDtoMapper
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.model.vote.request.VoteRequestMapper
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseMapper
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.StateVote
import com.example.fragmentvm.ui.utils.VotesEnum
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
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
        getCats()
    }

    @Inject
    lateinit var retrofit: RetrofitRepository

    @Inject
    lateinit var ds: DataStoreInterface

    private var _cats = MutableLiveData<List<CatDomain>>()
    val catsLiveData: LiveData<List<CatDomain>>
        get() = _cats

    fun vote(catModel: CatDomain, vote: VotesEnum, position: Int) {
        _stateUIVote.value = StateVote.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val voteRequest = VoteRequestMapper()
                .mapFromDomainModel(
                    VoteRequestDomain(catModel.id, vote)
                )
            retrofit.postVote(apikey, voteRequest)
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

    fun getCats() {
        _stateUIMain.value = StateMain.Loading

        viewModelScope.launch(Dispatchers.IO) {
            retrofit.getCats(apikey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            _stateUIMain.value = StateMain.Empty
                        } else {
                            _stateUIMain.value = StateMain.Finished
                            _cats.postValue(CatDtoMapper().toDomainList(it))
                        }
                    },
                    {
                        Timber.e(it)
                        _stateUIMain.value = StateMain.Error(it)
                    })
        }
    }

    fun resetVoteState() {
        _stateUIVote.value = StateVote.Empty
    }
}