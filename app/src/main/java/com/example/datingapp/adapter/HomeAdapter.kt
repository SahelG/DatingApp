package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.ChatActivity
import com.example.datingapp.Model.User
import com.example.datingapp.databinding.ItemUserLayoutBinding

class HomeAdapter (val context: Context, val list: ArrayList<User>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder (val binding: ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.binding.textView11.text = list[position].firstName
        holder.binding.textView4.text = list[position].email

        Glide.with(context).load(list[position].photo).into(holder.binding.userImage)

        holder.binding.btnChat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("idUser", list[position].idUser)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}