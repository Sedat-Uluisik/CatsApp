package com.sedat.catsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.repo.RepositoryCats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesFragmentViewModel @Inject constructor(
    private val repository: RepositoryCats
): ViewModel() {

    private val cats = MutableLiveData<List<CatItem>>()
    val catList: LiveData<List<CatItem>>
        get() = cats
    fun getDataFromRoom(){
        viewModelScope.launch {

            println("get cats From room in favorites fragment viewModel") //tek sefer

            val list = repository.getCatsFromRoom()
            cats.value = list
        }
    }

    fun deleteCatFromRoomWithId(id: String){
        viewModelScope.launch {

            println("delete cats From room in favorites fragment viewModel")

            repository.deleteCatFromRoom(id)
        }
    }

}