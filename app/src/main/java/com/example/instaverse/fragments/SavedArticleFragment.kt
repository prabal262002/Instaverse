package com.example.instaverse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instaverse.Models.userModel
import com.example.instaverse.adapters.MySavedArticleAdapter
import com.example.instaverse.databinding.FragmentSavedArticleBinding
import com.example.instaverse.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class SavedArticleFragment : Fragment() {

    private lateinit var binding: FragmentSavedArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedArticleBinding.inflate(inflater, container, false)
        val postList = ArrayList<String>()
        val adapter = MySavedArticleAdapter(requireContext(), postList)
        binding.rv.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val userModel = it.toObject<userModel>()!!
                val tempList = userModel.savedArticles
                postList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        return binding.root
    }

}