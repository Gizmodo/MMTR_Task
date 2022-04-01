package com.example.fragmentvm.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fragmentvm.App
import com.example.fragmentvm.core.utils.Constants
import com.example.fragmentvm.data.repository.CatRepository
import com.example.fragmentvm.domain.DataStoreInterface
import com.example.fragmentvm.temp.CatResponseItem
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val CATAPI_STARTING_PAGE_INDEX = 1

class CatPagingSource : PagingSource<Int, CatResponseItem>() {
    private var apikey: String

    init {
        App.instance().appGraph.embed(this)
        apikey = runBlocking { ds.getString(Constants.DataStore.KEY_API).toString() }
    }

    @Inject
    lateinit var repository: CatRepository

    @Inject
    lateinit var ds: DataStoreInterface

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatResponseItem> {
        val position = params.key ?: CATAPI_STARTING_PAGE_INDEX
        return try {
            val response = repository.searchRepos(apikey, position, params.loadSize)
            val repos = response
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == CATAPI_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CatResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}