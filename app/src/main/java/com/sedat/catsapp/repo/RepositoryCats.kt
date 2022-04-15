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

            println("get cat from api in repo") //tek sefer - paging ile birden çok çalışabilir

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

            println("search cats from api in repo")

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

        println("save cat from room in repo") //tek sefer

        dao.saveCatFromRoom(catItem)
    }

    suspend fun deleteCatFromRoom(id: String){

        println("delete cat from room in repo")

        dao.deleteCatFromRoomWithId(id)
    }

    suspend fun getCatsFromRoom(): List<CatItem>{

        println("get cat lis from room in repo") //tek sefer

        return dao.getCatsFromRoom()
    }

    suspend fun getCatsFromRoom2(callBack: (List<CatItem>) -> Unit){

        println("get cat list2 from room in repo with callback") //tek sefer

        val data = dao.getCatsFromRoom()
        callBack(data)
    }

    suspend fun isFavorite(id: String): Boolean{

        println("is favorite from room in repo") //tek sefer

        val catItem = dao.isFavorite(id)
        return catItem != null
    }
}