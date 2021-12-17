package com.example.fragmentvm.utils

import com.example.fragmentvm.network.RetrofitClient
import com.example.fragmentvm.network.RetrofitServices

object Common {
    const val BASE_URL = "https://api.thecatapi.com/v1/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}

