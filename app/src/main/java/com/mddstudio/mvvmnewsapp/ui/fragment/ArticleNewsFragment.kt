package com.mddstudio.mvvmnewsapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mddstudio.mvvmnewsapp.R
import com.mddstudio.mvvmnewsapp.ui.NewsActivity
import com.mddstudio.mvvmnewsapp.utils.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleNewsFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args:ArticleNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            viewModel = (activity as NewsActivity).viewModel!!
        } catch (e: Exception) {

        }


        requireActivity(). window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val article=args.article


        val settings =webView.settings

        settings.apply {
            domStorageEnabled=true
            javaScriptEnabled =false
            setAppCacheEnabled(true)
        }

        webView.apply {
            webViewClient= WebViewClient()

        }
        article.url?.let { webView.loadUrl(it) }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Toast.makeText(requireContext(), "Article Saved", Toast.LENGTH_SHORT).show()
        }

    }
}