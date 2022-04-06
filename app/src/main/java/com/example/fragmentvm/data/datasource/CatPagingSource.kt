package com.example.fragmentvm.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.data.model.cat.CatDto
import com.example.fragmentvm.data.model.cat.CatDtoMapper
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.domain.model.cat.CatDomain
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CatPagingSource : PagingSource<Int, CatDomain>() {
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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatDomain> {
        return try {
            val nextPage = params.key ?: 1
            val response: List<CatDto> = catRepository.getCats(apikey, nextPage, params.loadSize)
            val repos: List<CatDomain> = CatDtoMapper().toDomainList(response)
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

    override fun getRefreshKey(state: PagingState<Int, CatDomain>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}