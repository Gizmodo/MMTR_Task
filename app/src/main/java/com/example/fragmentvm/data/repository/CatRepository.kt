package com.example.fragmentvm.data.repository

import com.example.fragmentvm.core.utils.NetworkResult
import com.example.fragmentvm.core.utils.RxUtils
import com.example.fragmentvm.core.utils.api
import com.example.fragmentvm.data.model.cat.CatDtoMapper
import com.example.fragmentvm.data.model.favourite.delete.FavouriteResponseDeleteMapper
import com.example.fragmentvm.data.model.favourite.get.FavCatMapper
import com.example.fragmentvm.data.model.favourite.post.FavouriteRequestMapper
import com.example.fragmentvm.data.model.favourite.post.FavouriteResponseMapper
import com.example.fragmentvm.data.model.login.LoginDtoMapper
import com.example.fragmentvm.data.model.response.BackendResponseDtoMapper
import com.example.fragmentvm.data.model.vote.request.VoteRequestMapper
import com.example.fragmentvm.data.model.vote.response.VoteResponseMapper
import com.example.fragmentvm.data.service.CatService
import com.example.fragmentvm.domain.model.BackendResponseDomain
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteRequestDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDeleteDomain
import com.example.fragmentvm.domain.model.favourite.FavouriteResponseDomain
import com.example.fragmentvm.domain.model.login.LoginDomain
import com.example.fragmentvm.domain.model.vote.VoteRequestDomain
import com.example.fragmentvm.domain.model.vote.VoteResponseDomain
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
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

    suspend fun postVote(
        apiKey: String,
        vote: VoteRequestDomain,
    ): NetworkResult<VoteResponseDomain> = api(VoteResponseMapper()) {
        apiService.vote(
            apiKey,
            VoteRequestMapper().mapFromDomainModel(vote)
        )
    }

    suspend fun postFavourite(
        apiKey: String,
        payload: FavouriteRequestDomain,
    ): NetworkResult<FavouriteResponseDomain> = api(FavouriteResponseMapper()) {
        apiService.postFavourite(
            apiKey,
            FavouriteRequestMapper().mapFromDomainModel(payload)
        )
    }

    suspend fun deleteFavourite(
        apiKey: String,
        id: Int,
    ): NetworkResult<FavouriteResponseDeleteDomain> = api(FavouriteResponseDeleteMapper()) {
        apiService.deleteFavourite(apiKey, id)
    }
}
