package com.example.fragmentvm.di

import androidx.lifecycle.MutableLiveData
import com.example.fragmentvm.model.Cat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RetroServiceInterface {
    @GET("images/search")
    fun getFiveCatsLD(
        @Query("limit") limit: Int = 5,
        @Query("size") size: String = "small"
    ): Call<MutableLiveData<MutableList<Cat>>>

}