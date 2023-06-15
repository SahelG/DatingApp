package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.ChatActivity
import com.example.datingapp.Model.User
import com.example.datingapp.databinding.UserItemLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatUserAdapter(val context: Context, val list: ArrayList<String>, val chatKey: List<String>): RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder>() {
    inner class ChatUserViewHolder (val binding: UserItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        return ChatUserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val data = snapshot.getValue(User::class.java)

                            Glide.with(context).load(data!!.photo).into(holder.binding.userImage)

                            holder.binding.userName.text = data.firstName
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                    }

                }
            )

        holder.itemView.setOnClickListener {
            val inte = Intent(context, ChatActivity::class.java)
            inte.putExtra("chat_id", chatKey[position])
            inte.putExtra("idUser", list[position])
            context.startActivity(inte)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}