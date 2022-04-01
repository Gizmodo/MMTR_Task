package com.example.fragmentvm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.core.utils.Util
import com.example.fragmentvm.data.model.cat.CatDtoMapper
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.model.vote.request.VoteRequestMapper
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.UiAction
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import com.example.fragmentvm.temp.CatResponseItem
import com.example.fragmentvm.ui.utils.StateMain
import com.example.fragmentvm.ui.utils.StateVote
import com.example.fragmentvm.ui.utils.VotesEnum
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

private const val VISIBLE_THRESHOLD = 5
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "Android"

class MainViewModel : ViewModel() {

    private val _stateUIMain = MutableStateFlow<StateMain>(StateMain.Empty)
    fun getStateUIMain(): StateFlow<StateMain> = _stateUIMain

    private val _stateUIVote = MutableStateFlow<StateVote<VoteResponseDomain>>(StateVote.Empty)
    fun getStateUIVote(): StateFlow<StateVote<VoteResponseDomain>> = _stateUIVote

    private var apikey: String

//    val state: StateFlow<UiState>
//    val pagingDataFlow: Flow<PagingData<CatResponseItem>>

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
        val initialQuery: String = DEFAULT_QUERY
        val lastQueryScrolled: String = DEFAULT_QUERY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }
        pagingDataFlow = searches
            .flatMapLatest { searchRepo(queryString = it.query) }
            .cachedIn(viewModelScope)

        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

//        getCats()
    }

    @Inject
    lateinit var repository: CatRepository

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

    fun getCats() {
        _stateUIMain.value = StateMain.Loading

        viewModelScope.launch(Dispatchers.IO) {
            repository.getCats(apikey)
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

    private fun searchRepo(queryString: String): Flow<PagingData<CatResponseItem>> =
        repository.searchReposNew()

    override fun onCleared() {
//        savedStateHandle[LAST_SEARCH_QUERY] = state.value.query
//        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }
}