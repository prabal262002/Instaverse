package com.example.instaverse.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaverse.ChatActivity
import com.example.instaverse.Models.userModel
import com.example.instaverse.R
import com.example.instaverse.databinding.SearchRv2Binding

class SearchAdapter2(var context: Context, private var userList: ArrayList<userModel>) :
    RecyclerView.Adapter<SearchAdapter2.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRv2Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRv2Binding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(userList[position].image).placeholder(R.drawable.profile)
            .into(holder.binding.profile)
        holder.binding.textView3.text = userList[position].name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name",userList[position].name)
            intent.putExtra("email",userList[position].email)

            context.startActivity(intent)
        }

    }

}