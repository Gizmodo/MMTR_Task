package com.example.fragmentvm.data

import com.example.fragmentvm.core.utils.RxUtils
import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val apiService: RetrofitInterface,
) {
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
