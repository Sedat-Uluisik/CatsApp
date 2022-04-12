package com.sedat.catsapp.repo

import com.sedat.catsapp.api.CatApi
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.util.Resource
import javax.inject.Inject

class RepositoryCats @Inject constructor(
    private val catApi: CatApi
) {

    suspend fun getCats(page: Int): Resource<List<CatItem>>{
        return try {
            val response = catApi.getCats(page)

            if(response.isSuccessful){

                response.body()?.let {

                    //println(it.size)

                    return@let Resource.success(it)
                } ?: Resource.error("Error!", null)
            }else{
                Resource.error("Error!", null)
            }
        }catch (e: Exception){
            Resource.error("No data!", null)
        }
    }
}