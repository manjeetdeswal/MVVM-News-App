package com.mddstudio.mvvmnewsapp.data.api

import com.mddstudio.mvvmnewsapp.data.entity.NewsResponse
import com.mddstudio.mvvmnewsapp.utils.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "in",
        @Query("page") pageNo:Int=1,
        @Query("apiKey")apiKey:String=Constant.API_KEY
    ):Response<NewsResponse>


    @GET("v2/everything")
    suspend fun serachForNews(
        @Query("q")
        searchQuery: String ,
        @Query("page") pageNo:Int=1,
        @Query("apiKey")apiKey:String=Constant.API_KEY
    ):Response<NewsResponse>



}