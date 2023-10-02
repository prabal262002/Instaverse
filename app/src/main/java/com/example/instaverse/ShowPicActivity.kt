package com.example.instaverse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.instaverse.databinding.ActivityShowPicBinding
import com.squareup.picasso.Picasso

class ShowPicActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityShowPicBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        val uid = intent.getStringExtra("picUrl")
        Picasso.get().load(uid).placeholder(R.drawable.loading)
            .into(binding.picShow)

    }
}