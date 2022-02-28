package com.example.fragmentvm.network

import com.example.fragmentvm.model.backend.BackendResponse
import com.example.fragmentvm.model.cat.CatModel
import com.example.fragmentvm.model.vote.VotePayload
import com.example.fragmentvm.screen.login.LoginModel
import com.example.fragmentvm.utils.RxUtils
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val apiService: RetrofitInterface,
) {
    fun postSignUp(loginModel: LoginModel): @NonNull Observable<BackendResponse> {
        return apiService.signUp(loginModel)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun getFavourites(apikey: String): @NonNull Observable<List<BackendResponse>> {
        return apiService.favourites(apikey)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun getCats(apiKey: String): @NotNull Observable<List<CatModel>> {
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
