package com.sedat.catsapp.api

import com.sedat.catsapp.model.CatItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatApi {

    @GET("/v1/breeds")
    suspend fun getCats(
        @Query("page") page: Int,
        @Query("attach_breed") attach_breed: Int = 0,
        @Query("limit") limit: Int = 15,
        //@Header("x-api-key") apiKey: String = "eb572777-3a92-4cd4-9932-6767c362bea6"
    ): Response<List<CatItem>>

    @GET("/v1/breeds/search")
    suspend fun search(
        @Query("q") query: String
    ): Response<List<CatItem>>

}