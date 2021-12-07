package com.example.fragmentvm.network

import androidx.lifecycle.LiveData
import com.example.fragmentvm.model.CatItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitServices {

    @GET("images/search")
    @Headers(
        "x-api-key: 6aad15c4-b124-4ec3-846c-2c76f69cf5e8",
        "Content-Type: application/json"
    )
    fun getCatList(): Call<MutableList<CatItem>>

    @GET("images/search")
    @Headers(
        "x-api-key: 6aad15c4-b124-4ec3-846c-2c76f69cf5e8",
        "Content-Type: application/json"
    )
    fun getCatList2(): Call<LiveData<MutableList<CatItem>>>

    @GET("images/search")
    @Headers(
        "x-api-key: 6aad15c4-b124-4ec3-846c-2c76f69cf5e8",
        "Content-Type: application/json"
    )
    fun getCatListRV(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small"
    ): Call<MutableList<CatItem>>

    @GET("images/search")
    @Headers(
        "x-api-key: 6aad15c4-b124-4ec3-846c-2c76f69cf5e8",
        "Content-Type: application/json"
    )
    fun getFiveCats(
        @Query("limit") limit: Int = 15,
        @Query("size") size: String = "thumb"
    ): Call<List<CatItem>>
}