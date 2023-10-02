package com.example.instaverse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instaverse.Models.Reel
import com.example.instaverse.adapters.ReelAdapter
import com.example.instaverse.databinding.FragmentReelBinding
import com.example.instaverse.utils.REEL
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase




class ReelFragment : Fragment() {
   private lateinit var binding: FragmentReelBinding
   lateinit var adapter: ReelAdapter
   private var reelList = ArrayList<Reel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReelBinding.inflate(inflater, container, false)

        adapter = ReelAdapter(requireContext(),reelList)
        binding.viewPager.adapter = adapter
        Firebase.firestore.collection(REEL).get().addOnSuccessListener {
            val tempList = ArrayList<Reel>()
            reelList.clear()
            for (i in it.documents){
                val reel = i.toObject<Reel>()!!
                tempList.add(reel)
            }
            tempList.shuffle()
            reelList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

}