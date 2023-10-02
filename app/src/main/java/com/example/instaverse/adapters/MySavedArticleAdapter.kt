package com.example.instaverse.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instaverse.Models.Post
import com.example.instaverse.R
import com.example.instaverse.ShowPicActivity
import com.example.instaverse.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MySavedArticleAdapter(var context: Context, private var postList: ArrayList<String>) :
    RecyclerView.Adapter<MySavedArticleAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyPostRvDesignBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postList[position]).placeholder(R.drawable.loading)
            .into(holder.binding.postImageRv)

        holder.binding.postImageRv.setOnClickListener {
            val intent = Intent(context, ShowPicActivity::class.java)
            intent.putExtra("picUrl", postList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}