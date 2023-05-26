package com.example.storyapp.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.api.Stories
import com.example.storyapp.view.detail.DetailActivity

class ListStoryAdapter(private var stories: List<Stories>): RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    fun setData(newStories: List<Stories>) {
        stories = newStories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listStory = stories[position]
        Glide.with(holder.itemView.context)
            .load(listStory.photoUrl)
            .into(holder.photoUser)
        holder.nameUser.text = listStory.name
        holder.descUser.text = listStory.description
        holder.itemView.setOnClickListener {
            val moveToDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetail.putExtra(DetailActivity.EXTRA_USERNAME, listStory.name)
            moveToDetail.putExtra(DetailActivity.EXTRA_DESCRIPTION, listStory.description)
            moveToDetail.putExtra(DetailActivity.EXTRA_URL, listStory.photoUrl)
            holder.itemView.context.startActivity(moveToDetail)
        }
    }

    override fun getItemCount(): Int = stories.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoUser: ImageView = itemView.findViewById(R.id.photo)
        val nameUser: TextView = itemView.findViewById(R.id.name)
        val descUser: TextView = itemView.findViewById(R.id.description)
    }

}

