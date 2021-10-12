package com.mddstudio.mvvmnewsapp.data.room

import androidx.room.TypeConverter
import javax.xml.transform.Source

class Converter {

    @TypeConverter
    fun fromSource(source: com.mddstudio.mvvmnewsapp.data.entity.Source): String {
        return source.name
    }

    @TypeConverter
    fun ToSource(name:String):com.mddstudio.mvvmnewsapp.data.entity.Source{
        return com.mddstudio.mvvmnewsapp.data.entity.Source(name,name)
    }
}