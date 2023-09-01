package com.example.instaverse

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instaverse.Models.Reel
import com.example.instaverse.Models.userModel
import com.example.instaverse.databinding.ActivityAddReelBinding
import com.example.instaverse.utils.REEL
import com.example.instaverse.utils.REEL_FOLDER
import com.example.instaverse.utils.USER_NODE
import com.example.instaverse.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AddReelActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAddReelBinding.inflate(layoutInflater)
    }
    private var videoUrl: String? = null
    lateinit var progressDialog: ProgressDialog
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            checkVideoDuration(uri)
        }
    }

    private fun checkVideoDuration(uri: Uri) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, uri)
        mediaPlayer.setOnPreparedListener {
            val durationInMillis = mediaPlayer.duration.toLong()
            val durationInSeconds = durationInMillis / 1000

            if (durationInSeconds > 120) {
                // Video duration exceeds the allowed limit
                Toast.makeText(this, "Video duration is too long", Toast.LENGTH_SHORT).show()
            } else {
                // Video duration is within the allowed limit, proceed to upload
                uploadVideo(uri, REEL_FOLDER, progressDialog) { url ->
                    if (url != null) {
                        videoUrl = url
                    }
                }
            }

            mediaPlayer.release()
        }
        mediaPlayer.prepareAsync()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@AddReelActivity, HomeActivity::class.java))
            finish()
        }

        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@AddReelActivity, HomeActivity::class.java))
            finish()
        }
        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    val user = it.toObject<userModel>()!!

                    val reel: Reel = Reel(videoUrl!!, binding.addCaption.editText?.text.toString(),user.image!!)

                    Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL)
                            .document()
                            .set(reel)
                            .addOnSuccessListener {
                                startActivity(
                                    Intent(
                                        this@AddReelActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            }
                    }
                }

        }

    }
}