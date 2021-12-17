package com.example.fragmentvm.di

import com.example.fragmentvm.model.Cat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RetroServiceInterface {
    @GET("images/search")
    fun getCats(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small"
    ): Call<List<Cat>>
}