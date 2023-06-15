package com.example.datingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.Model.User
import com.example.datingapp.adapter.ChatUserAdapter
import com.example.datingapp.adapter.HomeAdapter
import com.example.datingapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager: CardStackLayoutManager

    private lateinit var recyclerView: RecyclerView

    //private lateinit var list: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //init()
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getData()
        return binding.root
    }

    private fun init () {

        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                if (manager.topPosition == list!!.size) {
                    Toast.makeText(requireContext(), "Haz visto todos los usuarios", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCardRewound() {}

            override fun onCardCanceled() {}

            override fun onCardAppeared(view: View?, position: Int) {}

            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)

    }

    companion object {
        var list: ArrayList<User>? = null
    }

    private fun getData () {
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list = arrayListOf()
                        for (data in snapshot.children) {
                            val model = data.getValue(User::class.java)
                            if (model!!.email != FirebaseAuth.getInstance().currentUser!!.email)
                                list!!.add(model!!)
                        }

                        list!!.shuffle()
                        init()
                        binding.cardStackView.layoutManager = manager
                        binding.cardStackView.itemAnimator = DefaultItemAnimator()
                        //binding.cardStackView.adapter = HomeAdapter(requireContext(), list!!)
                        binding.cardStackView.adapter = context?.let { HomeAdapter(it, list!!) }

                    } else {
                        Toast.makeText(requireContext(), "Algo salio mal", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                }
            })
    }


}