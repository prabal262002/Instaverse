package com.example.instaverse.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaverse.Models.Post
import com.example.instaverse.Models.userModel
import com.example.instaverse.R
import com.example.instaverse.ShowPicActivity
import com.example.instaverse.databinding.PostRvBinding
import com.example.instaverse.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(var context: Context, private var postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {
    inner class MyHolder(var binding: PostRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    private var userTemp: userModel? = null
    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
//        var user: userModel?
        try {
            Firebase.firestore.collection(USER_NODE).document(postList[position].uid).get()
                .addOnSuccessListener {
                    val user = it.toObject<userModel>()
                    if(user!=null) {
                        userTemp = user
                        Glide.with(context).load(user.image).placeholder(R.drawable.user)
                            .into(holder.binding.profileImage)
                        holder.binding.name.text = user.name
                    }
                    else{
                        Glide.with(context).load(R.drawable.user).into(holder.binding.profileImage)
                        holder.binding.name.text = "Admin"
                    }
                }
        } catch (e: Exception) {
            Log.d("GADHE", "onBindViewHolder: EROOREEEEEEEEEE")
        }

        Glide.with(context).load(postList[position].postUrl).placeholder(R.drawable.loading)
            .into(holder.binding.imagePost)
        try {
            val textTime: String = TimeAgo.using(postList[position].time.toLong())
            holder.binding.time.text = textTime
        } catch (e: Exception) {
            holder.binding.time.text = ""
        }
        holder.binding.caption.text = postList[position].caption
        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.like_red)
        }
        holder.binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, postList[position].postUrl)
            context.startActivity(intent)
        }
        holder.binding.imagePost.setOnClickListener {
            val intent = Intent(context, ShowPicActivity::class.java)
            intent.putExtra("picUrl", postList[position].postUrl)
            context.startActivity(intent)
        }

        holder.binding.profileImage.setOnClickListener {
            if (userTemp?.image.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val intent = Intent(context, ShowPicActivity::class.java)
            intent.putExtra("picUrl", userTemp?.image)
            context.startActivity(intent)
        }

        holder.binding.saveArticle.setOnClickListener {

            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    val userModel = it.toObject<userModel>()!!
                    if (userModel.savedArticles.contains(postList[position].postUrl)) {
                        return@addOnSuccessListener
                    }
                    userModel.savedArticles.add(postList[position].postUrl)
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(userModel)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Article Saved.", Toast.LENGTH_SHORT).show()
                        }
                }

        }

    }
}