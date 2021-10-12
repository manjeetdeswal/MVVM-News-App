package com.mddstudio.mvvmnewsapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mddstudio.mvvmnewsapp.R
import com.mddstudio.mvvmnewsapp.ui.NewsActivity
import com.mddstudio.mvvmnewsapp.utils.Constant.QUER_PAGE
import com.mddstudio.mvvmnewsapp.utils.adapter.NewsAdapter
import com.mddstudio.mvvmnewsapp.utils.network.Resource
import com.mddstudio.mvvmnewsapp.utils.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import retrofit2.Response

class BreakinNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    lateinit var scrollListener: RecyclerView.OnScrollListener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layouManger = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItem = layouManger.findFirstVisibleItemPosition()
                val VisibleItem = layouManger.childCount
                val totalItem = layouManger.itemCount

                val isnotLastPageNotLoading = !isLastPage && !isLoading
                val islastItem = firstVisibleItem + VisibleItem >= totalItem
                val isnotAtBeginng = firstVisibleItem >= 0
                val istotalMOreThanVisible = totalItem >= 20

                val shouldloadPage = isnotLastPageNotLoading && islastItem && isnotAtBeginng
                        && istotalMOreThanVisible && isScrolling

                if (shouldloadPage) {
                    viewModel.getBreakingNews("in")
                    isScrolling = false
                }


            }
        }




        try {
            viewModel = (activity as NewsActivity).viewModel!!


        setupUI()

        newsAdapter.setonitemClickListner {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakinNewsFragment_to_articleNewsFragment,
                bundle
            )
        }
        viewModel.breakingNewsLive.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hidePorgress()
                    it.let { newsresponse ->
                        newsAdapter.differList.submitList(newsresponse.data?.articles?.toList())
                        val totalPages = newsresponse.data?.totalResults!! / QUER_PAGE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0, 0, 0, 0)
                        }


                    }
                }
                is Resource.Error -> {
                    hidePorgress()
                    it.message?.let {
                        Toast.makeText(requireContext(), "Error" + it, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showPorgress()
                }
            }
        })

        } catch (e: Exception) {

        }

    }

    private fun hidePorgress() {
        isLoading = false
        paginationProgressBar.isVisible = false

    }

    private fun showPorgress() {
        paginationProgressBar.isVisible = true
        isLoading = true
    }


    private fun setupUI() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
            addOnScrollListener(this@BreakinNewsFragment.scrollListener)
        }

    }
}