package com.example.instaverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.instaverse.Models.userModel
import com.example.instaverse.databinding.ActivitySignUpBinding
import com.example.instaverse.utils.USER_NODE
import com.example.instaverse.utils.USER_PROFILE_FOLDER
import com.example.instaverse.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var userModel: userModel
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER){
                if (it!=null){
                    userModel.image = it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text = "<font color=#ff000000>Already  hava an account. </font> <font color=#1e88e5>Login?</font>"
        binding.login.setText(Html.fromHtml(text))
        userModel = userModel()
        if (intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){

                binding.signUpButton.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        userModel = it.toObject<userModel>()!!
                        if(!userModel.image.isNullOrEmpty()){
                            Picasso.get().load(userModel.image).into(binding.profileImage)
                        }
                        binding.textField.editText?.setText(userModel.name)
                        binding.emailField.editText?.setText(userModel.email)
                        binding.passwordField.editText?.setText(userModel.password)
                    }
            }
        }
        binding.signUpButton.setOnClickListener {
            if (intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(userModel)
                        .addOnSuccessListener {
                            startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                        }
                }
            }

            if ((binding.textField.editText?.text.toString() == "") or
                (binding.passwordField.editText?.text.toString() == "") or
                (binding.emailField.editText?.text.toString() == "")
            ) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please fill all the information", Toast.LENGTH_SHORT
                ).show()
            } else {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailField.editText?.text.toString(),
                    binding.passwordField.editText?.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {

                        userModel.name = binding.textField.editText?.text.toString()
                        userModel.password = binding.passwordField.editText?.text.toString()
                        userModel.email = binding.emailField.editText?.text.toString()

                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid).set(userModel)
                            .addOnSuccessListener {
                                Toast.makeText(
                                this@SignUpActivity,
                                "Login Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignUpActivity,HomeActivity::class.java))
                            }

                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            result.exception?.localizedMessage, Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

        binding.profileImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
        }
    }
}