package com.mddstudio.mvvmnewsapp.data.api

import com.mddstudio.mvvmnewsapp.utils.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class NewsInstance {

    companion object{
        private val retrofit by lazy {
            val loggingInterceptor =HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val clinet =OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clinet).build()


        }
        val apiInstance by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}