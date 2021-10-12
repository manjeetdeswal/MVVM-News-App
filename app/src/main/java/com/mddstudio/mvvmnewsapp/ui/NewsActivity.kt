package com.mddstudio.mvvmnewsapp.ui

import android.app.Dialog
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.mddstudio.mvvmnewsapp.R
import com.mddstudio.mvvmnewsapp.data.repo.NewsRepository
import com.mddstudio.mvvmnewsapp.data.room.NewsDatabase
import com.mddstudio.mvvmnewsapp.ui.fragment.ArticleNewsFragment
import com.mddstudio.mvvmnewsapp.utils.viewmodel.NewsViewModel
import com.mddstudio.mvvmnewsapp.utils.viewmodel.NewsViewModelFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

     var viewModel: NewsViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN ,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        try {



        val connectivityManager =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected || !networkInfo.isAvailable) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.alert_dialog)
            dialog.setCancelable(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            val button = dialog.findViewById<Button>(R.id.btn)
            button.setOnClickListener { recreate() }
            dialog.show()
        }
        else {
            bottomNavigationView.setupWithNavController(newsNavHost.findNavController())
            val repository = NewsRepository(NewsDatabase.getInstance(this))
            val factory = NewsViewModelFactory(repository)
            viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)
        }

        }catch (e:Exception){
                e.printStackTrace()
            }

        }









    }

