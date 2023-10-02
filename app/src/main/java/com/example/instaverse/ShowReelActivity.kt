package com.example.instaverse

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.instaverse.databinding.ActivityShowReelBinding

class ShowReelActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityShowReelBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_reel)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        val uid = intent.getStringExtra("reelUrl")
        Log.d("VideoUri", "Video URI: $uid")
        binding.videoView.setVideoPath(uid)
        Log.d("tum kyun", "onCreate: working")
        binding.videoView.setOnErrorListener { mp, what, extra ->
            Log.e("VideoError", "Error occurred: what=$what, extra=$extra")
            true
        }
        binding.videoView.setOnPreparedListener {
        Log.d("tum kyun2", "onCreate: working")
            binding.progressBar.visibility = View.GONE
            binding.videoView.start()
            it.isLooping = true
        }
    }
}