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

            println("isFav kontrol in details viewModel") //tek sefer

            val bool = repository.isFavorite(id)
            callBack(bool)
        }
    }

    fun saveCatFromRoom(catItem: CatItem){
        viewModelScope.launch {

            println("save cat from romm in details viewModel")

            repository.saveCatFromRoom(catItem)
        }
    }

    fun deleteCatFromRoomWithId(id: String) =viewModelScope.launch {

        println("delete cat from room in details viewModel")

        repository.deleteCatFromRoom(id)
    }
}