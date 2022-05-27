package com.example.fragmentvm.data.repository

import com.example.fragmentvm.core.utils.RxUtils
import com.example.fragmentvm.data.model.cat.CatDtoMapper
import com.example.fragmentvm.data.model.favourite.delete.FavouriteResponseDeleteDto
import com.example.fragmentvm.data.model.favourite.get.FavCatMapper
import com.example.fragmentvm.data.model.favourite.post.FavoriteRequestDto
import com.example.fragmentvm.data.model.favourite.post.FavouriteResponseDto
import com.example.fragmentvm.data.model.login.LoginDtoMapper
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.model.vote.request.VoteRequestDto
import com.example.fragmentvm.data.model.vote.response.VoteResponseDto
import com.example.fragmentvm.data.service.CatService
import com.example.fragmentvm.domain.model.BackendResponseDomain
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.domain.model.login.LoginDomain
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.annotations.NotNull
import retrofit2.Response
import javax.inject.Inject

class CatRepository @Inject constructor(
    private val apiService: CatService,
) {
    suspend fun getCats(key: String, page: Int, itemsPerPage: Int): List<CatDomain> {
        return apiService.getCats(apiKey = key, page = page, itemsPerPage = itemsPerPage)
            .map {
                CatDtoMapper().mapToDomainModel(it)
            }
    }

    suspend fun getFavouriteCats(apiKey: String, page: Int, itemsPerPage: Int): List<FavCatDomain> {
        return apiService.getFavouriteCats(apiKey, page, itemsPerPage)
            .map {
                FavCatMapper().mapToDomainModel(it)
            }
    }

    fun postSignUp(login: LoginDomain): @NonNull Observable<BackendResponseDomain> {
        val loginModel = LoginDtoMapper().mapFromDomainModel(login)
        return apiService.signUp(loginModel).map {
            BackendResponseDtoMapper().mapToDomainModel(it)
        }.compose(RxUtils.applySubscriberScheduler())
    }

    fun sendApiKey(apikey: String): @NonNull Observable<List<FavCatDomain>> {
        return apiService.getApiKey(apikey)
            .map { FavCatMapper().toDomainList(it) }
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun postVote(
        apiKey: String,
        vote: VoteRequestDto,
    ): @NotNull Observable<Response<VoteResponseDto>> {
        return apiService.vote(apiKey, vote)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun postFavourite(
        apiKey: String,
        payload: FavoriteRequestDto,
    ): @NotNull Observable<Response<FavouriteResponseDto>> {
        return apiService.postFavourite(apiKey, payload)
            .compose(RxUtils.applySubscriberScheduler())
    }

    fun deleteFavourite(
        apiKey: String,
        id: Int,
    ): @NotNull Observable<Response<FavouriteResponseDeleteDto>> {
        return apiService.deleteFavourite(apiKey, id)
            .compose(RxUtils.applySubscriberScheduler())
    }
}
