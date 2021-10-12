package com.mddstudio.mvvmnewsapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mddstudio.mvvmnewsapp.data.entity.Article

@Database(entities = [Article::class], version = 5)

@TypeConverters(Converter::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao


    companion object {
        private var instance: NewsDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NewsDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, NewsDatabase::class.java,
                    "article"
                ).fallbackToDestructiveMigration().build()

            }
            return instance!!
        }


    }

}