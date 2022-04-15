package com.sedat.catsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: RepositoryCats
): ViewModel() {

    private val cats = MutableLiveData<Flow<PagingData<CatItem>>>()
    val catList: LiveData<Flow<PagingData<CatItem>>>
        get() = cats

    fun getData(){
        val data = Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                CatPagingSource(repository)
            }
        ).flow.cachedIn(viewModelScope)

        cats.value = data
    }

    private val searchData = MutableLiveData<List<CatItem>>()
    val search: LiveData<List<CatItem>>
        get() = searchData

    fun search(query: String){
        if(!query.isNullOrEmpty()){
            viewModelScope.launch {

                val data = repository.search(query)
                if(data.data != null && data.data.isNotEmpty()){
                    searchData.value = data.data!!
                }
            }
        }
    }

    fun clearSearchItems(){
        searchData.value = listOf()
    }

    fun saveCatFromRoom(catItem: CatItem){
        viewModelScope.launch {
            repository.saveCatFromRoom(catItem)
        }
    }
    fun deleteCatFromRoomWithId(id: String) = viewModelScope.launch {
        repository.deleteCatFromRoom(id)
    }

    fun getCatsFromRoom(callBack: (List<CatItem>) -> Unit){
        viewModelScope.launch {
            repository.getCatsFromRoom2 {
                callBack(it)
            }
        }
    }

    fun isFavorite(id: String, callBack: (Boolean)-> Unit){
        viewModelScope.launch {
            val bool = repository.isFavorite(id)
            callBack(bool)
        }
    }

}