package com.example.fragmentvm.data.service

import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface CatService {
    @GET("images/search")
    suspend fun searchRepos(
        @Header("x-api-key") apiKey: String,
        @Query("page") page: Int,
        @Query("limit") itemsPerPage: Int = 2,
    ): List<CatDto>

    @GET("images/search")
    fun getCatsObservable(
        @Header("x-api-key") apiKey: String,
        @Query("limit") limit: Int = 10,
        @Query("size") size: String = "small",
    ): Observable<List<CatDto>>

    @POST("user/passwordlesssignup")
    fun signUp(
        @Body document: LoginDto,
    ): Observable<BackendResponseDto>

    @GET("favourites")
    fun getApiKey(
        @Query("api_key") apiKey: String,
    ): Observable<List<BackendResponseDto>>

    @POST("votes")
    fun vote(
        @Header("x-api-key") apiKey: String,
        @Body document: VoteRequestDto,
    ): Observable<Response<VoteResponseDto>>
}