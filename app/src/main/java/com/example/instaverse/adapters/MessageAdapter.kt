package com.example.instaverse.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instaverse.Models.Message
import com.example.instaverse.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==ITEM_RECEIVE){
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieved,parent,false)
            ReceiveViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        val senderMail = FirebaseAuth.getInstance().currentUser?.email
        val currentSenderMail = currentMessage.senderMail
        Log.d("MessageAdapter", "SenderMail: $senderMail, CurrentSenderMail: $currentSenderMail")

        return if(FirebaseAuth.getInstance().currentUser?.email?.uppercase().equals(currentMessage.senderMail?.uppercase())){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        when (holder) {
            is SentViewHolder -> {
                holder.sentMessage.text = currentMessage.message
            }
            is ReceiveViewHolder -> {
                holder.receiveMessage.text = currentMessage.message
            }
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById(R.id.sent_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.received_message)
    }
    fun reverseList() {
        messageList.reverse()
    }
}