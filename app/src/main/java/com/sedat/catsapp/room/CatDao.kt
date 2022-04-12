package com.sedat.catsapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sedat.catsapp.model.CatItem

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCatFromRoom(catItem: CatItem)

    @Query("SELECT * FROM favorites")
    suspend fun getCatsFromRoom(): List<CatItem>

    @Query("SELECT * FROM favorites WHERE id =:id")
    suspend fun isFavorite(id: String): CatItem

    @Query("DELETE FROM favorites WHERE id =:id")
    suspend fun deleteCatFromRoomWithId(id: String)
}