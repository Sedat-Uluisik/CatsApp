package com.sedat.catsapp.util

import androidx.room.TypeConverter
import com.sedat.catsapp.model.Image

class ConverterFromImage {
    @TypeConverter
    fun fromImage(image: Image): String{
        return image.url.toString()
    }

    @TypeConverter
    fun toImage(url: String): Image{
        return Image(0, "", url, 0)
    }
}