package com.mddstudio.mvvmnewsapp.utils.viewmodel

import androidx.lifecycle.*
import com.mddstudio.mvvmnewsapp.data.entity.Article
import com.mddstudio.mvvmnewsapp.data.entity.NewsResponse
import com.mddstudio.mvvmnewsapp.data.repo.NewsRepository
import com.mddstudio.mvvmnewsapp.utils.network.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {


    init {
        getBreakingNews("in")
    }



 

    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    private var breakingNewsresponse :NewsResponse? =null
    val breakingNewsLive: LiveData<Resource<NewsResponse>>
        get() = _breakingNews

    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
     var searchNewsresponse :NewsResponse? =null
    val searchNewsLive: LiveData<Resource<NewsResponse>>
        get() = _searchNews



    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertArticle(article)
    }
    fun  clearList(){
        searchNewsresponse?.articles?.clear()
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getAllSavedNews() =
        newsRepository.getAllArticle()






    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        _searchNews.postValue(handleSearchResponse(response))
    }


    fun getBreakingNews(country: String) = viewModelScope.launch {

        val response = newsRepository.getBreakingNews(country, breakingNewsPage)
        _breakingNews.postValue(Resource.Loading())
        _breakingNews.postValue(handleNewsResponse(response))

    }


    private fun handleNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { respone ->
                breakingNewsPage++
                if (breakingNewsresponse ==null){
                    breakingNewsresponse =respone

                }else{
                    val oldArticles =breakingNewsresponse?.articles
                    val newArticle=respone.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsresponse ?: respone)
            }

        }
        return Resource.Error(response.message())
    }

    private fun handleSearchResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            searchNewsPage++
            response.body()?.let { respone ->
                if (searchNewsresponse ==null){
                    searchNewsresponse =respone

                }else{
                    val oldArticles =searchNewsresponse?.articles
                    val newArticle=respone.articles
                    oldArticles?.addAll(newArticle)
                }
                return Resource.Success(searchNewsresponse ?: respone)
            }

        }
        return Resource.Error(response.message())
    }


}


class NewsViewModelFactory(val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository = newsRepository) as T
    }

}