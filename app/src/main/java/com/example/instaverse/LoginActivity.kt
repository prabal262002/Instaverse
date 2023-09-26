package com.example.instaverse

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instaverse.Models.userModel
import com.example.instaverse.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            if (binding.emailLogin.editText?.text.toString().equals("") or
                binding.passLogin.editText?.text.toString().equals("")
            ) {
                Toast.makeText(
                    this@LoginActivity,
                    "Please fill all the details",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var user = userModel(
                    binding.emailLogin.editText?.text.toString(),
                    binding.passLogin.editText?.text.toString()
                )
                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
        }
    }
}