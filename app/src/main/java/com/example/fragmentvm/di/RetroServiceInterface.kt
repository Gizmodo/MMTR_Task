package com.example.fragmentvm.di

import com.example.fragmentvm.model.BackendResponse
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.model.VotePayload
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*


interface RetroServiceInterface {

    @GET("images/search")
    fun getCatsObservable(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int = 2,
        @Query("size") size: String = "small",
    ): Observable<List<Cat>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: Payload,
    ): Observable<BackendResponse>

    @GET("favourites")
    fun favourites(
        @Query("api_key") apiKey: String,
    ): Observable<List<BackendResponse>>

    @POST("votes")
    fun vote(
        @Header("x-api-key") apiKey: String,
        @Body document: VotePayload,
    ): Observable<BackendResponse>
}