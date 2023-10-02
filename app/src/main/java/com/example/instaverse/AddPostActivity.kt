package com.example.instaverse

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instaverse.Models.Post
import com.example.instaverse.Models.userModel
import com.example.instaverse.databinding.ActivityAddPostBinding
import com.example.instaverse.utils.POST
import com.example.instaverse.utils.POST_FOLDER
import com.example.instaverse.utils.USER_NODE
import com.example.instaverse.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AddPostActivity : AppCompatActivity() {
        private val binding by lazy {
        ActivityAddPostBinding.inflate(layoutInflater)
    }
    private var imageUrl: String? = null
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.selectPost.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@AddPostActivity, HomeActivity::class.java))
            finish()
        }

        binding.selectPost.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@AddPostActivity, HomeActivity::class.java))
            finish()
        }
        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                .get().addOnSuccessListener {
                    val user = it.toObject<userModel>()!!
                    val post = Post(
                        postUrl = imageUrl!!,
                        caption = binding.addCaption.editText?.text.toString(),
                        uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )

                    Firebase.firestore.collection(POST).document().set(post)
                        .addOnSuccessListener {
                            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid)
                                .document()
                                .set(post)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@AddPostActivity,
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