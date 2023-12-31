package com.example.instaverse.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaverse.Messaging
import com.example.instaverse.Models.Post
import com.example.instaverse.adapters.PostAdapter
import com.example.instaverse.databinding.FragmentHomeBinding
import com.example.instaverse.utils.POST
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private var postList=ArrayList<Post>()
    private lateinit var adapter:PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostAdapter(requireContext(),postList)
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())
        binding.postRv.adapter = adapter

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            val tempList = ArrayList<Post>()
            postList.clear()
            for (i in it.documents){
                val post:Post = i.toObject<Post>()!!
                tempList.add(post)
            }
            tempList.shuffle()
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        binding.send.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), Messaging::class.java))
        }
        return binding.root
    }


}