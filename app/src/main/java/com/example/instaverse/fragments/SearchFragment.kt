package com.example.instaverse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaverse.Models.userModel
import com.example.instaverse.adapters.SearchAdapter
import com.example.instaverse.databinding.FragmentSearchBinding
import com.example.instaverse.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    lateinit var searchAdapter: SearchAdapter
    var userList = ArrayList<userModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            val tempList = ArrayList<userModel>()
            userList.clear()
            for (i in it.documents) {
                if (i.id != Firebase.auth.currentUser!!.uid) {
                    val user: userModel = i.toObject<userModel>()!!
                    tempList.add(user)
                }
            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        binding.searchButton.setOnClickListener {
            val text = binding.searchView.text.toString().lowercase()
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name", text).get()
                .addOnSuccessListener {
                    val tempList = ArrayList<userModel>()
                    userList.clear()
                    if (!it.isEmpty) {
                        for (i in it.documents) {
                            if (i.id != Firebase.auth.currentUser!!.uid) {
                                val user: userModel = i.toObject<userModel>()!!
                                tempList.add(user)
                            }
                        }
                        userList.addAll(tempList)
                        adapter.notifyDataSetChanged()
                    }
                }
        }
        return binding.root
    }

}