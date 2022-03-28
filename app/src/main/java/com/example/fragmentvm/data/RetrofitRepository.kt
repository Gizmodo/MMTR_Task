package com.example.fragmentvm.data

import com.example.fragmentvm.core.utils.RxUtils
import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.model.backend.BackendResponse
import com.example.fragmentvm.model.vote.VotePayload
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val apiService: RetrofitInterface,
) {
    fun postSignUp(loginDto: LoginDto): @NonNull Observable<BackendResponse> {
        return apiService.signUp(loginDto)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun getFavourites(apikey: String): @NonNull Observable<List<BackendResponse>> {
        return apiService.favourites(apikey)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun getCats(apiKey: String): @NotNull Observable<List<CatDto>> {
        return apiService.getCatsObservable(apiKey)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun postVote(
        apiKey: String,
        votePayload: VotePayload,
    ): @NotNull Observable<Response<BackendResponse>> {
        return apiService.vote(apiKey, votePayload)
            .compose(RxUtils.applySubscriberScheduler())
    }
}
