package com.mddstudio.mvvmnewsapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mddstudio.mvvmnewsapp.data.entity.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article):Long

    @Query("select * from articles")
     fun getAllArticles():LiveData<List<Article>>

     @Delete
     suspend fun delete(article: Article)

}