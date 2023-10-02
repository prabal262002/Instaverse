package com.example.instaverse.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaverse.Models.userModel
import com.example.instaverse.R
import com.example.instaverse.databinding.SearchRvBinding
import com.example.instaverse.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

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
        Glide.with(context).load(userList[position].image).placeholder(R.drawable.user)
            .into(holder.binding.profile)
        holder.binding.textView3.text = userList[position].name

        var userModel = userModel()
        var itSelfUser: DocumentSnapshot? = null
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                userModel = it.toObject<userModel>()!!
                itSelfUser = it
                if (userModel.following.contains(
                        userList[position].email?.lowercase().toString()
                    )
                ) {
                    holder.binding.followButton.visibility = View.GONE
                    holder.binding.unfollowButton.visibility = View.VISIBLE
                }
            }

        holder.binding.followButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    userModel = it.toObject<userModel>()!!
                    itSelfUser = it
                    if (userModel.following.contains(
                            userList[position].email?.lowercase().toString()
                        )
                    ) {
                        return@addOnSuccessListener
                    }
                    userModel.following.add(userList[position].email?.lowercase().toString())
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(userModel)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Now You are following ${userList[position].name}.", Toast.LENGTH_SHORT)
                                .show()
                        }

                    Firebase.firestore.collection(USER_NODE).get()
                        .addOnSuccessListener { QuerySnapShot ->

                            for (i in QuerySnapShot.documents) {
                                val email = i["email"] as String
                                if (email.lowercase() == userList[position].email?.lowercase()
                                        .toString()
                                ) {
                                    val uid = i.id
                                    userList[position].followers.add(userModel.email?.lowercase().toString())
                                    Firebase.firestore.collection(USER_NODE)
                                        .document(uid).set(userList[position])
                                        .addOnSuccessListener {
                                            holder.binding.followButton.visibility = View.GONE
                                            holder.binding.unfollowButton.visibility = View.VISIBLE
                                        }
                                    break
                                }
                            }
                        }
                }
        }

        holder.binding.unfollowButton.setOnClickListener {

            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    userModel = it.toObject<userModel>()!!
                    if (!userModel.following.contains(
                            userList[position].email?.lowercase().toString()
                        )
                    ) {
                        return@addOnSuccessListener
                    }

                    userModel.following.remove(userList[position].email?.lowercase().toString())
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(userModel)
                        .addOnSuccessListener {

                        }

                    Firebase.firestore.collection(USER_NODE).get()
                        .addOnSuccessListener { QuerySnapShot ->

                            for (i in QuerySnapShot.documents) {
                                val email = i["email"] as String
                                if (email.lowercase() == userList[position].email?.lowercase()
                                        .toString()
                                ) {
                                    val uid = i.id
                                    userList[position].followers.remove(userModel.email?.lowercase().toString())
                                    Log.d("Hello", "onBindViewHolder: ${userModel.email?.lowercase().toString()}")
                                    Firebase.firestore.collection(USER_NODE)
                                        .document(uid).set(userList[position])
                                        .addOnSuccessListener {
                                            holder.binding.followButton.visibility = View.VISIBLE
                                            holder.binding.unfollowButton.visibility = View.GONE
                                            Toast.makeText(context, "Now You are not following ${userList[position].name}.", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    break
                                }
                            }
                        }
                }
        }


    }
}