package com.example.fragmentvm.di

import com.example.fragmentvm.model.Cat
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface RetroServiceInterface {
    @GET("images/search")
    fun getCats(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small",
    ): Single<List<Cat>>
}