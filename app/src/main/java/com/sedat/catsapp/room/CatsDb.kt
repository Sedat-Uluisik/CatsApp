package com.sedat.catsapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.util.ConverterFromImage
import com.sedat.catsapp.util.ConverterFromWeight

@Database(entities = [CatItem::class], exportSchema = false, version = 1)
@TypeConverters(ConverterFromImage::class, ConverterFromWeight::class)
abstract class CatsDb: RoomDatabase() {
    abstract fun catDao(): CatDao

    companion object{
        @Volatile
        private var instance: CatsDb ?= null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: Room.databaseBuilder(
                context,
                CatsDb::class.java,
                "cats_db"
            ).build().also {
                instance = it
            }
        }
    }
}