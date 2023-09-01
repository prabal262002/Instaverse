package com.example.instaverse.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaverse.Models.userModel
import com.example.instaverse.R
import com.example.instaverse.databinding.SearchRvBinding

class SearchAdapter(var context: Context, private var userList: ArrayList<userModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(userList[position].image).placeholder(R.drawable.profile)
            .into(holder.binding.profile)
        holder.binding.textView3.text = userList[position].name

    }
}