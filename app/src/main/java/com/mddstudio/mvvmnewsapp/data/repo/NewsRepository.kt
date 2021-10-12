package com.mddstudio.mvvmnewsapp.data.repo

import androidx.lifecycle.LiveData
import com.mddstudio.mvvmnewsapp.data.api.NewsInstance
import com.mddstudio.mvvmnewsapp.data.entity.Article
import com.mddstudio.mvvmnewsapp.data.entity.NewsResponse
import com.mddstudio.mvvmnewsapp.data.room.NewsDatabase
import com.mddstudio.mvvmnewsapp.utils.Constant.API_KEY

import retrofit2.Response

class NewsRepository(val newsDatabase: NewsDatabase) {

    suspend fun getBreakingNews(country:String,pageno:Int):Response<NewsResponse> =NewsInstance.apiInstance.getBreakingNews(country,pageno,API_KEY)

    suspend fun searchNews(searchQuery:String,pageno:Int) =NewsInstance.apiInstance.serachForNews(searchQuery,pageno)

    suspend fun insertArticle(article: Article) =newsDatabase.getNewsDao().insert(article)

    suspend fun deleteArticle(article: Article) =newsDatabase.getNewsDao().delete(article)

     fun getAllArticle() =newsDatabase.getNewsDao().getAllArticles()
}
