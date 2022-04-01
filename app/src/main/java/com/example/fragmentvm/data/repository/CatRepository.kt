package com.example.fragmentvm.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fragmentvm.core.utils.RxUtils
import com.example.fragmentvm.data.CatPagingSource
import com.example.fragmentvm.data.CatPagingSource.Companion.NETWORK_PAGE_SIZE
import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import com.example.fragmentvm.data.service.CatService
import com.example.fragmentvm.temp.CatResponse
import com.example.fragmentvm.temp.CatResponseItem
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import javax.inject.Inject

class CatRepository @Inject constructor(
    private val apiService: CatService,
) {
    suspend fun searchRepos(key: String, page: Int, itemsPerPage: Int): CatResponse {
        return apiService.searchRepos(apiKey = key, page = page, itemsPerPage = itemsPerPage)
    }

    fun searchReposNew(): Flow<PagingData<CatResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CatPagingSource() }
        ).flow
    }

    fun postSignUp(loginDto: LoginDto): @NonNull Observable<BackendResponseDto> {
        return apiService.signUp(loginDto)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun sendApiKey(apikey: String): @NonNull Observable<List<BackendResponseDto>> {
        return apiService.getApiKey(apikey)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun getCats(apiKey: String): @NotNull Observable<List<CatDto>> {
        return apiService.getCatsObservable(apiKey)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun postVote(
        apiKey: String,
        vote: VoteRequestDto,
    ): @NotNull Observable<Response<VoteResponseDto>> {
        return apiService.vote(apiKey, vote)
            .compose(RxUtils.applySubscriberScheduler())
    }
}
