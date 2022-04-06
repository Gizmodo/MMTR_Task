package com.example.fragmentvm.data.repository

import com.example.fragmentvm.core.utils.RxUtils
import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import com.example.fragmentvm.data.service.CatService
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import javax.inject.Inject

class CatRepository @Inject constructor(
    private val apiService: CatService,
) {
    suspend fun getCats(key: String, page: Int, itemsPerPage: Int): List<CatDto> {
        return apiService.getCats(apiKey = key, page = page, itemsPerPage = itemsPerPage)
    }

    fun postSignUp(loginDto: LoginDto): @NonNull Observable<BackendResponseDto> {
        return apiService.signUp(loginDto)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun sendApiKey(apikey: String): @NonNull Observable<List<BackendResponseDto>> {
        return apiService.getApiKey(apikey)
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
