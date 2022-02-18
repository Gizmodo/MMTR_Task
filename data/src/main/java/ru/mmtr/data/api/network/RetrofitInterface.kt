package ru.mmtr.data.api.network

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*
import ru.mmtr.domain.model.BackendResponse
import ru.mmtr.domain.model.Cat
import ru.mmtr.domain.model.LoginPayload
import ru.mmtr.domain.model.VotePayload

interface RetrofitInterface {

    @GET("images/search")
    fun getCatsObservable(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int = 20,
        @Query("size") size: String = "small",
    ): Observable<List<Cat>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: LoginPayload,
    ): Observable<BackendResponse>

    @GET("favourites")
    fun favourites(
        @Query("api_key") apiKey: String,
    ): Observable<List<BackendResponse>>

    @POST("votes")
    fun vote(
        @Header("x-api-key") apiKey: String,
        @Body document: VotePayload,
    ): Observable<Response<BackendResponse>>
}