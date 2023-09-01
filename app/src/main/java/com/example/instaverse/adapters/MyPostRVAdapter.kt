package com.example.instaverse.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instaverse.Models.Post
import com.example.instaverse.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MyPostRVAdapter(var context: Context, private var postList: ArrayList<Post>) :
    RecyclerView.Adapter<MyPostRVAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postList[position].postUrl).into(holder.binding.postImageRv)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}