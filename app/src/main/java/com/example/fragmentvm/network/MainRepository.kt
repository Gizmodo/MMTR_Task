package com.example.fragmentvm.network

class MainRepository constructor(private val retrofitService: RetrofitServices) {
    fun getFiveRandomCats() = retrofitService.getFiveCats()
}