package com.example.datingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.datingapp.adapter.ChatUserAdapter
import com.example.datingapp.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatsBinding.inflate(layoutInflater)

        getData()

        //binding.recyclerView.adapter = ChatUserAdapter(requireContext(), list, newList)

        return binding.root
    }

    private fun getData() {
        //var list = arrayListOf<String>()
        //var newList = arrayListOf<String>()
        val currentId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var list = arrayListOf<String>()
                    var newList = arrayListOf<String>()

                    for (data in snapshot.children) {
                        if (data.key!!.contains(currentId!!)) {
                            list.add(data.key!!.replace(currentId!!, ""))
                            newList.add(data.key!!)
                        }
                    }

                    binding.recyclerView.adapter = context?.let { ChatUserAdapter(it, list, newList) }
                    /*try {
                        binding.recyclerView.adapter = ChatUserAdapter(requireContext(), list, newList)
                    }catch (e: Exception) { }*/

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                }

            })
    }

}