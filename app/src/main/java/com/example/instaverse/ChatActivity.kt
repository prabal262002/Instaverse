package com.example.instaverse

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaverse.Models.Message
import com.example.instaverse.adapters.MessageAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private var receiverRoom: String? = null
    private var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverMail = intent.getStringExtra("email").toString().uppercase()
        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString().uppercase()



        receiverRoom = senderMail + receiverMail
        senderRoom = receiverMail + senderMail


        val toolbar = findViewById<MaterialToolbar>(R.id.materialToolbar)
        toolbar.title = name
        messageRecyclerView = findViewById(R.id.messageRV)


        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this@ChatActivity, messageList)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        Firebase.firestore.collection("chats").document(senderRoom!!)
            .collection("messages")
            .orderBy("timestamp",Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                messageList.clear()
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val messageData = document.toObject(Message::class.java)
                        if (messageData != null) {
                            messageList.add(messageData)
                        }
                    }
                }
                messageAdapter.reverseList()
                messageAdapter.notifyDataSetChanged()
            }

        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            if(message=="") return@setOnClickListener

            val messageObject = Message(message, senderMail, Date())

            Firebase.firestore.collection("chats").document(senderRoom!!)
                .collection("messages")
                .add(messageObject)
                .addOnSuccessListener {
                    Firebase.firestore.collection("chats").document(receiverRoom!!)
                        .collection("messages")
                        .add(messageObject)
                        .addOnSuccessListener {
                            messageBox.setText("") // Clear the message input field
                        }
                }


        }

    }
}