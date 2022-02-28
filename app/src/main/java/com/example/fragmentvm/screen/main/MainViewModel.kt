package com.example.fragmentvm.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.datastore.DataStoreRepository
import com.example.fragmentvm.model.backend.BackendResponse
import com.example.fragmentvm.model.cat.CatModel
import com.example.fragmentvm.model.states.StateMain
import com.example.fragmentvm.model.states.StateVote
import com.example.fragmentvm.model.vote.VotePayload
import com.example.fragmentvm.model.vote.VotesEnum
import com.example.fragmentvm.network.RetrofitRepository
import com.example.fragmentvm.utils.Constants.DataStore.KEY_API
import com.example.fragmentvm.utils.Util.parseResponseError
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class MainViewModel : ViewModel() {
    private val _stateUIMain = MutableStateFlow<StateMain>(StateMain.Empty)
    fun getStateUIMain(): StateFlow<StateMain> = _stateUIMain

    private val _stateUIVote = MutableStateFlow<StateVote<BackendResponse>>(StateVote.Empty)
    fun getStateUIVote(): StateFlow<StateVote<BackendResponse>> = _stateUIVote

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(KEY_API).toString() }
        getCats()
    }

    @Inject
    lateinit var retrofit: RetrofitRepository

    @Inject
    lateinit var ds: DataStoreRepository

    private var _cats = MutableLiveData<List<CatModel>>()
    val catsLiveData: LiveData<List<CatModel>>
        get() = _cats

    fun vote(catModel: CatModel, vote: VotesEnum, position: Int) {
        _stateUIVote.value = StateVote.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val votePayload = VotePayload(catModel.id, vote.value)
            retrofit.postVote(apikey, votePayload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let { body ->
                            body.position = position
                            body.vote = vote
                            _stateUIVote.value = StateVote.Success(body)
                        }
                    } else {
                        it.errorBody()?.let { body ->
                            Timber.d("Body response not null")
                            try {
                                val error = parseResponseError(body)
                                error.position = position
                                error.vote = vote
                                _stateUIVote.value = StateVote.BadResponse(error)
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }
                    }
                }, {
                    if (it is HttpException) {
                        try {
                            val error = parseResponseError(it.response()?.errorBody())
                            error.position = position
                            error.vote = vote
                            _stateUIVote.value = StateVote.BadResponse(error)
                        } catch (e: Exception) {
                            Timber.e(it)
                            _stateUIVote.value = StateVote.Error(it)
                        }
                    }
                    Timber.e(it)
                    _stateUIVote.value = StateVote.Error(it)
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
                            _cats.postValue(it)
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