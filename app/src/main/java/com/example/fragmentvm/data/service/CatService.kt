package com.example.fragmentvm.data.service

import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.favourite.delete.FavouriteResponseDeleteDto
import com.example.fragmentvm.data.model.favourite.get.FavCatDto
import com.example.fragmentvm.data.model.favourite.post.FavouriteRequestDto
import com.example.fragmentvm.data.model.favourite.post.FavouriteResponseDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CatService {
    @GET("images/search")
    suspend fun getCats(
        @Header("x-api-key") apiKey: String,
        @Query("page") page: Int,
        @Query("limit") itemsPerPage: Int = 10,
    ): List<CatDto>

    // Get the Favourites belonging to your account, with the option to filter by ‘sub_id’ used when creating them.
    @GET("favourites")
    suspend fun getFavouriteCats(
        @Header("x-api-key") apiKey: String,
        @Query("page") page: Int,
        @Query("limit") itemsPerPage: Int = 10,
    ): List<FavCatDto>

    @POST("user/passwordlesssignup")
    suspend fun signUp(
        @Body document: LoginDto,
    ): Response<BackendResponseDto>

    @GET("favourites")
    fun getApiKey(
        @Query("api_key") apiKey: String,
    ): Observable<List<FavCatDto>>

    @POST("votes")
    suspend fun vote(
        @Header("x-api-key") apiKey: String,
        @Body document: VoteRequestDto,
    ): Response<VoteResponseDto>

    @POST("favourites")
    suspend fun postFavourite(
        @Header("x-api-key") apiKey: String,
        @Body document: FavouriteRequestDto,
    ): Response<FavouriteResponseDto>

    @DELETE("favourites/{favourite_id}")
    suspend fun deleteFavourite(
        @Header("x-api-key") apiKey: String,
        @Path("favourite_id") favourite_id: Int,
    ): Response<FavouriteResponseDeleteDto>
}
