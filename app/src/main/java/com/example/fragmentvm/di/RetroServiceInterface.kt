package com.example.fragmentvm.di

import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.model.SignupResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetroServiceInterface {
    @GET("images/search")
    fun getCats(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small",
    ): Single<List<Cat>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: Payload,
    ): Observable<SignupResponse>
}