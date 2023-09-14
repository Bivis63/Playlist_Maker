package com.example.playlistmaker.media.data.impl.converters

import androidx.room.TypeConverter

class TracksConverter {
    @TypeConverter
    fun fromArrayList(list: ArrayList<Long>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toArrayList(data: String): ArrayList<Long> {
        val list = ArrayList<Long>()
        val items = data.split(",").toTypedArray()
        for (item in items) {
            if (item.isNotEmpty()) {
                list.add(item.toLong())
            }
        }
        return list
    }
}