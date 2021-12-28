package com.example.fragmentvm.di

import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.model.Payload
import com.example.fragmentvm.model.Signup
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetroServiceInterface {
    @GET("images/search")
    fun getCats(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small",
//        @Query("api_key") apiKey: String,
    ): Single<List<Cat>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: Payload,
    ): Observable<Signup>

    @GET("favourites")
    fun favourites(
        @Query("api_key") apiKey:String
    ) : Observable<List<Signup>>
}