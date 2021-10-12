package com.mddstudio.mvvmnewsapp.data.entity

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    var totalResults: Int
)