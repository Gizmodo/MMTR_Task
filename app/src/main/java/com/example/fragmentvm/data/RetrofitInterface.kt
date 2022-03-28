package com.example.fragmentvm.data

import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.vote.VoteDto
import com.example.fragmentvm.model.backend.BackendResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface RetrofitInterface {

    @GET("images/search")
    fun getCatsObservable(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int = 20,
        @Query("size") size: String = "small",
    ): Observable<List<CatDto>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: LoginDto,
    ): Observable<BackendResponse>

    @GET("favourites")
    fun favourites(
        @Query("api_key") apiKey: String,
    ): Observable<List<BackendResponse>>

    @POST("votes")
    fun vote(
        @Header("x-api-key") apiKey: String,
        @Body document: VoteDto,
    ): Observable<Response<BackendResponse>>
}