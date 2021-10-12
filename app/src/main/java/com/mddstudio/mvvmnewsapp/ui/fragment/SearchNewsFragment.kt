package com.mddstudio.mvvmnewsapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mddstudio.mvvmnewsapp.R
import com.mddstudio.mvvmnewsapp.ui.NewsActivity
import com.mddstudio.mvvmnewsapp.utils.Constant
import com.mddstudio.mvvmnewsapp.utils.adapter.NewsAdapter
import com.mddstudio.mvvmnewsapp.utils.network.Resource
import com.mddstudio.mvvmnewsapp.utils.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.view.inputmethod.EditorInfo

import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi


class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            viewModel = (activity as NewsActivity).viewModel!!


        setupUI()

        var job:Job?= null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(10)
                if (it != null && it.isNotEmpty()){
                    etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            viewModel.clearList()
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                            viewModel.searchNews(it.toString())
                            return@OnEditorActionListener true
                        }
                        false
                    })

                }

            }
        }





        viewModel.searchNewsLive.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    hidePorgress()
                    it.let {newsresponse->
                        //newsAdapter.differList.currentList.clear()
                        newsAdapter.differList.submitList(newsresponse.data?.articles)

                    }
                }
                is Resource.Error -> {
                    hidePorgress()
                    it.message?.let {
                        Toast.makeText(requireContext(), "Error"+it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showPorgress()
                }
            }
        })
        newsAdapter.setonitemClickListner {
            val bundle= Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleNewsFragment,bundle)
        }
    }

    private fun hidePorgress() {
        paginationProgressBarSea.isVisible = false

    }

    private fun showPorgress() {
        paginationProgressBarSea.isVisible = true

    }


    private fun setupUI() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter

        }

    }
}