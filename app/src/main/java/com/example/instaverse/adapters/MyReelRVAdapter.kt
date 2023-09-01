package com.example.instaverse.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.instaverse.Models.Post
import com.example.instaverse.Models.Reel
import com.example.instaverse.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MyReelRVAdapter(var context: Context, private var reelList: ArrayList<Reel>) :
    RecyclerView.Adapter<MyReelRVAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(reelList[position].reelUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.postImageRv)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }
}