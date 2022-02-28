package com.example.fragmentvm.network

import com.example.fragmentvm.model.backend.BackendResponse
import com.example.fragmentvm.model.cat.CatModel
import com.example.fragmentvm.model.vote.VotePayload
import com.example.fragmentvm.screen.login.LoginModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface RetrofitInterface {

    @GET("images/search")
    fun getCatsObservable(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int = 20,
        @Query("size") size: String = "small",
    ): Observable<List<CatModel>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: LoginModel,
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