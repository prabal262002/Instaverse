package com.example.instaverse.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instaverse.AddPostActivity
import com.example.instaverse.AddReelActivity
import com.example.instaverse.R
import com.example.instaverse.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(layoutInflater)
        binding.post.setOnClickListener{
            activity?.startActivity(Intent(requireContext(),AddPostActivity::class.java))
            activity?.finish()
        }
        binding.reel.setOnClickListener{
            activity?.startActivity(Intent(requireContext(), AddReelActivity::class.java))
        }
        return binding.root
    }

    companion object {

    }
}