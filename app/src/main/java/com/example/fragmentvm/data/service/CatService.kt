package com.example.fragmentvm.data.service

import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.favourite.get.FavouritesList
import com.example.fragmentvm.data.model.favourite.post.FavoriteRequestDto
import com.example.fragmentvm.data.model.favourite.post.FavouriteResponseDto
import com.example.fragmentvm.data.model.login.LoginDto
import com.example.fragmentvm.data.model.response.BackendResponseDto
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface CatService {
    @GET("images/search")
    suspend fun getCats(
        @Header("x-api-key") apiKey: String,
        @Query("page") page: Int,
        @Query("limit") itemsPerPage: Int = 10,
    ): List<CatDto>

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

    //Save an Image as a Favourite to your Account
    @POST("favourites")
    fun postFavourite(
        @Header("x-api-key") apiKey: String,
        @Body document: FavoriteRequestDto,
    ): Observable<Response<FavouriteResponseDto>>

    //Get the Favourites belonging to your account, with the option to filter by ‘sub_id’ used when creating them.
    @GET("favourites")
    fun getFavourites(
        @Header("x-api-key") apiKey: String,
    ): Observable<Response<FavouritesList>>

    //Delete the Favourite with the ID passed if it belongs to your Account.
    @DELETE("favourites/:favourite_id")
    fun deleteFavourite(
        @Header("x-api-key") apiKey: String,
        @Query("favourite_id") favourite_id: String
    )
}