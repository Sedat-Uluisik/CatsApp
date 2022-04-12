package com.sedat.catsapp.util

import androidx.room.TypeConverter
import com.sedat.catsapp.model.Weight

class ConverterFromWeight {
    @TypeConverter
    fun fromWeight(weight: Weight): String{
        return weight.metric.toString()
    }

    @TypeConverter
    fun toWeight(metric: String): Weight {
        return Weight("", metric)
    }
}