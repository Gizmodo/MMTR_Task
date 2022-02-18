package ru.mmtr.data.api.network

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import ru.mmtr.domain.model.BackendResponse
import ru.mmtr.domain.model.Cat
import ru.mmtr.domain.model.LoginPayload
import ru.mmtr.domain.model.VotePayload
import ru.mmtr.domain.utils.RxUtils
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val apiService: ru.mmtr.data.api.network.RetrofitInterface,
) {
    fun postSignUp(loginPayload: LoginPayload): @NonNull Observable<BackendResponse> {
        return apiService.signUp(loginPayload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFavourites(apikey: String): @NonNull Observable<List<BackendResponse>> {
        return apiService.favourites(apikey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getCats(apiKey: String): @NotNull Observable<List<Cat>> {
        return apiService.getCatsObservable(apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun postVote(
        apiKey: String,
        votePayload: VotePayload,
    ): @NotNull Observable<Response<BackendResponse>> {
        return apiService.vote(apiKey, votePayload)
            .compose(RxUtils.applyObservableScheduler())
    }
}
