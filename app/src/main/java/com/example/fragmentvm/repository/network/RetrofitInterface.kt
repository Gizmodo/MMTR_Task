package com.example.fragmentvm.repository.network

import com.example.fragmentvm.model.BackendResponse
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.LoginPayload
import com.example.fragmentvm.model.VotePayload
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

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