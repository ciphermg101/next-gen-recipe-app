package com.example.next_gen_recipe_app.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromListToJson(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun fromJsonToList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
