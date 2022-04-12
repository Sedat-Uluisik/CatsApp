package com.sedat.catsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.paging.CatPagingSource
import com.sedat.catsapp.repo.RepositoryCats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: RepositoryCats
): ViewModel() {

    fun catList(): Flow<PagingData<CatItem>>{
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                CatPagingSource(repository)
            }
        ).flow.cachedIn(viewModelScope)
    }

}