package com.example.instaverse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaverse.Models.userModel
import com.example.instaverse.adapters.SearchAdapter
import com.example.instaverse.adapters.SearchAdapter2
import com.example.instaverse.databinding.ActivityMessagingBinding
import com.example.instaverse.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class Messaging : AppCompatActivity() {
    private val binding by lazy {
        ActivityMessagingBinding.inflate(layoutInflater)
    }
    lateinit var searchAdapter: SearchAdapter
    private var userList = ArrayList<userModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@Messaging, HomeActivity::class.java))
            finish()
        }

        binding.rvMessaging.layoutManager = LinearLayoutManager(this)
        val adapter = SearchAdapter2(this, userList)
        binding.rvMessaging.adapter = adapter
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val userModel = it.toObject<userModel>()!!
                Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {query->
                    val tempList = ArrayList<userModel>()
                    userList.clear()
                    for (i in query.documents) {
                        if (i.id != Firebase.auth.currentUser!!.uid) {
                            val user: userModel = i.toObject<userModel>()!!
                            if (userModel.following.contains(user.email?.lowercase().toString())){
                            tempList.add(user)
                            }
                        }
                    }
                    userList.addAll(tempList)
                    if (userList.isEmpty()) {
                        binding.rvMessaging.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                    } else {
                        binding.rvMessaging.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }


}