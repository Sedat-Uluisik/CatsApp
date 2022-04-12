package com.sedat.catsapp.repo

import android.content.Context
import com.sedat.catsapp.api.CatApi
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.room.CatsDb
import com.sedat.catsapp.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RepositoryCats @Inject constructor(
    private val catApi: CatApi,
    @ApplicationContext private val context: Context
) {

    private val dao = CatsDb(context).catDao()

    suspend fun getCats(page: Int): Resource<List<CatItem>>{
        return try {
            val response = catApi.getCats(page)

            if(response.isSuccessful){

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error!", null)
            }else{
                Resource.error("Error!", null)
            }
        }catch (e: Exception){
            Resource.error("No data!", null)
        }
    }

    suspend fun search(query: String): Resource<List<CatItem>>{
        return try {
            val response = catApi.search(query)
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error!", null)
            }else{
                Resource.error("Error!", null)
            }
        }catch (e: Exception){
            Resource.error("No data!", null)
        }
    }

    suspend fun saveCatFromRoom(catItem: CatItem){
        dao.saveCatFromRoom(catItem)
    }

    suspend fun deleteCatFromRoom(id: String){
        dao.deleteCatFromRoomWithId(id)
    }

    suspend fun getCatsFromRoom(): List<CatItem>{
        return dao.getCatsFromRoom()
    }

    suspend fun isFavorite(id: String): Boolean{
        val catItem = dao.isFavorite(id)
        return catItem != null
    }
}