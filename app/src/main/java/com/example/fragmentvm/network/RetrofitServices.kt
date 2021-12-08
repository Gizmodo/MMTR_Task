package com.example.fragmentvm.network

import androidx.lifecycle.LiveData
import com.example.fragmentvm.model.Cat
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitServices {

    @GET("images/search")
    @Headers("Content-Type: application/json")
    fun getCatList(): Call<MutableList<Cat>>

    @GET("images/search")
    @Headers("Content-Type: application/json")
    fun getCatList2(): Call<LiveData<MutableList<Cat>>>

    @GET("images/search")
    @Headers("Content-Type: application/json")
    fun getCatListRV(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small"
    ): Call<MutableList<Cat>>

    @GET("images/search")
    @Headers("Content-Type: application/json")
    fun getFiveCats(
        @Query("limit") limit: Int = 15,
        @Query("size") size: String = "thumb"
    ): Call<List<Cat>>

    /**
     * RxJava
     */
    @GET("images/search")
    @Headers("Content-Type: application/json")
    fun getFiveCatsRxJava(
        @Query("limit") limit: Int = 15,
        @Query("size") size: String = "thumb"
    ): Single<List<Cat>>
}