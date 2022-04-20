package com.example.fragmentvm.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.data.model.favourite.get.FavCatDto
import com.example.fragmentvm.data.model.favourite.get.FavCatMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.favourite.list.FavCatDomain
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class FavCatPagingSource : PagingSource<Int, FavCatDomain>() {
    private var apikey: String
    override val keyReuseSupported: Boolean
        get() = true

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    @Inject
    lateinit var catRepository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface

    override fun getRefreshKey(state: PagingState<Int, FavCatDomain>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavCatDomain> {
        return try {
            val nextPage = params.key ?: 1
            val response: List<FavCatDto> = catRepository
                .getFavouriteCats(apikey, nextPage, params.loadSize)
            val repos: List<FavCatDomain> = FavCatMapper().toDomainList(response)
            LoadResult.Page(
                data = repos,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}