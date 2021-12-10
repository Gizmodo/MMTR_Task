package com.example.fragmentvm.network

import com.example.fragmentvm.model.Cat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitServices {
    @GET("images/search")
    @Headers("Content-Type: application/json")
    fun getFiveCats(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small"
    ): Call<List<Cat>>
}