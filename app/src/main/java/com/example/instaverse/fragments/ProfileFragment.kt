package com.example.instaverse.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.instaverse.Models.userModel
import com.example.instaverse.ShowPicActivity
import com.example.instaverse.SignUpActivity
import com.example.instaverse.adapters.viewPagerAdapter
import com.example.instaverse.databinding.FragmentProfileBinding
import com.example.instaverse.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: viewPagerAdapter
    private lateinit var userTemp:userModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)
            activity?.finish()
        }

        binding.profileImage.setOnClickListener {
            if (userTemp.image.isNullOrEmpty()) {
                return@setOnClickListener
            }
            val intent = Intent(context, ShowPicActivity::class.java)
            intent.putExtra("picUrl", userTemp.image)
            context?.startActivity(intent)
        }
        viewPagerAdapter = viewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragment(MyPostFragment(),"My Post")
        viewPagerAdapter.addFragment(MyReelsFragment(),"My Reels")
        viewPagerAdapter.addFragment(SavedArticleFragment(),"Saved Items")
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user: userModel = it.toObject<userModel>()!!
                userTemp = user
                binding.name.text = user.name
                binding.email.text = user.email
                binding.followersCount.text = userTemp.followers.size.toString()
                binding.followingCount.text = userTemp.following.size.toString()
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
            }
    }
}