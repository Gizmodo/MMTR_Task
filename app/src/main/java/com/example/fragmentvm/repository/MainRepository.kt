package com.example.fragmentvm.repository

import com.example.fragmentvm.network.RetrofitServices

class MainRepository constructor(private val retrofitService: RetrofitServices) {
    fun getFiveRandomCats() = retrofitService.getFiveCats()
}