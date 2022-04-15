package com.sedat.catsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.repo.RepositoryCats
import javax.inject.Inject

class CatPagingSource @Inject constructor(
    private val repository: RepositoryCats
): PagingSource<Int, CatItem>() {
    override fun getRefreshKey(state: PagingState<Int, CatItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatItem> {
        return try {
            val nextPage = params.key ?: 1
            val response = repository.getCats(nextPage)
            val data = if(response.data != null && response.data.isNotEmpty())
                response.data
            else
                listOf()

            println("load data from paging with api in CatPagingSource") //paging ile birden çok çalışabilir

            LoadResult.Page(
                data = data,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if(data.isEmpty()) null else nextPage + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}