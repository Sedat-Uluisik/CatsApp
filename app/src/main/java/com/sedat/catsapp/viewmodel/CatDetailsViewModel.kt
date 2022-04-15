package com.sedat.catsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.repo.RepositoryCats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatDetailsViewModel @Inject constructor(
    private val repository: RepositoryCats
): ViewModel(){

    fun isFavorite(id: String, callBack: (Boolean)-> Unit){
        viewModelScope.launch {
            val bool = repository.isFavorite(id)
            callBack(bool)
        }
    }

    fun saveCatFromRoom(catItem: CatItem){
        viewModelScope.launch {
            repository.saveCatFromRoom(catItem)
        }
    }

    fun deleteCatFromRoomWithId(id: String) =viewModelScope.launch {
        repository.deleteCatFromRoom(id)
    }
}