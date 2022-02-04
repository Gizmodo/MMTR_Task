package com.example.fragmentvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragmentvm.App
import com.example.fragmentvm.model.BackendResponse
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.VotePayload
import com.example.fragmentvm.repository.DataStoreRepositoryImpl
import com.example.fragmentvm.repository.RepositoryRetrofit
import com.example.fragmentvm.utils.StateUIMain
import com.example.fragmentvm.utils.StateUIVote
import com.example.fragmentvm.utils.VotesEnum
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class MainViewModel : ViewModel() {
    private val _stateUIMain = MutableStateFlow<StateUIMain>(StateUIMain.Empty)
    fun getStateUIMain() : StateFlow<StateUIMain> = _stateUIMain

    private val _stateUIVote = MutableStateFlow<StateUIVote<BackendResponse>>(StateUIVote.Empty)
    fun getStateUIVote() : StateFlow<StateUIVote<BackendResponse>> = _stateUIVote

    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString("apikey").toString() }
        getCats()
    }

    @Inject
    lateinit var retrofit: RepositoryRetrofit

    @Inject
    lateinit var ds: DataStoreRepositoryImpl

    private var _cats = MutableLiveData<List<Cat>>()
    val cats: LiveData<List<Cat>>
        get() = _cats

    // TODO: передать position и возвращать его в Success
    fun vote(cat: Cat, vote: VotesEnum, position: Int) {
        _stateUIVote.value = StateUIVote.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val votePayload = VotePayload(cat.id, vote.value)
            retrofit.postVoteWithResponse(apikey, votePayload)
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let { body ->
                            body.position=position
                            body.vote = vote
                            _stateUIVote.value = StateUIVote.Success(body)
                        }
                    } else {
                        Timber.d("400")
                        it.errorBody()?.let { body ->
                            Timber.d("Body response not null")
                            val gson = Gson()
                            val adapter: TypeAdapter<BackendResponse> =
                                gson.getAdapter(BackendResponse::class.java)
                            try {
                                val error: BackendResponse =
                                    adapter.fromJson(body.string())

                                _stateUIVote.value = StateUIVote.BadResponse(error)
                            } catch (e: IOException) {
                                Timber.e(e)
                                _stateUIVote.value = StateUIVote.Error(e)
                            }
                        }
                    }
                }, {
                    if (it is HttpException) {
                        val body = it.response()?.errorBody()
                        val gson = Gson()
                        val adapter: TypeAdapter<BackendResponse> =
                            gson.getAdapter(BackendResponse::class.java)
                        try {
                            val error: BackendResponse =
                                adapter.fromJson(body?.string())
                            _stateUIVote.value = StateUIVote.BadResponse(error)
                        } catch (e: IOException) {
                            Timber.e(it)
                            _stateUIVote.value = StateUIVote.Error(it)
                        }
                    }
                    Timber.e(it)
                    _stateUIVote.value = StateUIVote.Error(it)
                })

            /*  retrofit.postVote(apikey, votePayload = votePayload)
                  .subscribe({
                      if (it.message.contentEquals("SUCCESS")) {
                          _uiVoteState.value = UiState.Success(it)
                      } else {

                      }
                  }, {
                      if (it is HttpException) {
                          val body = it.response()?.errorBody()
                          val gson = Gson()
                          val adapter: TypeAdapter<BackendResponse> =
                              gson.getAdapter(BackendResponse::class.java)
                          try {
                              val error: BackendResponse =
                                  adapter.fromJson(body?.string())
                              _uiVoteState.value = UiState.BadResponse(error)
                          } catch (e: IOException) {
                              Timber.e(it)
                              _uiVoteState.value = UiState.Error(it)
                          }
                      }
                      Timber.e(it)
                      _uiVoteState.value = UiState.Error(it)
                  })*/
        }
    }

    fun getCats() {
        _stateUIMain.value = StateUIMain.Loading

        viewModelScope.launch(Dispatchers.IO) {
            retrofit.getCats(apikey)
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            _stateUIMain.value = StateUIMain.Empty
                        } else {
                            _stateUIMain.value = StateUIMain.Finished
                            _cats.postValue(it)
                        }
                    },
                    {
                        Timber.e(it)
                        _stateUIMain.value = StateUIMain.Error(it)
                    })
        }
    }
}