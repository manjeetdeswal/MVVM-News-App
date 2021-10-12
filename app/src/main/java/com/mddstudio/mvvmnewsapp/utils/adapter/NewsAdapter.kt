package com.mddstudio.mvvmnewsapp.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mddstudio.mvvmnewsapp.R
import com.mddstudio.mvvmnewsapp.data.entity.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    private val differCallback =object :DiffUtil.ItemCallback<Article>(){

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url ==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem ==newItem
        }
    }
    val differList =AsyncListDiffer(this,differCallback)




    inner class NewsHolder(itemview: View):RecyclerView.ViewHolder(itemview){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
       val view =LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false)
        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
       val article =differList.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text =article.source?.name
            tvDescription.text =article.description
            tvPublishedAt.text =article.publishedAt
            tvTitle.text =article.title
            holder.itemView.setOnClickListener {
                onItemClickListner?.let {
                    it(article)
                }
            }

        }
    }

    private var onItemClickListner:((Article) ->Unit)? =null
    fun setonitemClickListner(listner:(Article)->Unit){
        onItemClickListner=listner
    }

    override fun getItemCount(): Int {
       return differList.currentList.size
    }
}