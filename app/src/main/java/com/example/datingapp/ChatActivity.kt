package com.example.datingapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.datingapp.Api.ApiUtilities
import com.example.datingapp.Model.MessageModel
import com.example.datingapp.Model.NotificationData
import com.example.datingapp.Model.PushNotification
import com.example.datingapp.Model.User
import com.example.datingapp.adapter.MessageAdapter
import com.example.datingapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //getData(intent.getStringExtra("chat_id"))
        verifyChatId()

        binding.btnSendMessage.setOnClickListener {
            if (binding.txtYourMessage.text!!.isEmpty()) {
                Toast.makeText(this@ChatActivity, "Por favor ingresa tu mensaje", Toast.LENGTH_LONG).show()
            } else {
                storeData(binding.txtYourMessage.text.toString())
            }
        }
    }

    private var senderId: String? = null
    private var chatId: String? = null
    private var receiverId: String? = null

    private fun verifyChatId() {
        receiverId = intent.getStringExtra("idUser")
        senderId = FirebaseAuth.getInstance().currentUser!!.uid

        chatId = senderId + receiverId
        val reverseChatId = receiverId + senderId


        val reference = FirebaseDatabase.getInstance().getReference("chats")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.hasChild(chatId!!)) {
                    getData(chatId)
                } else if (snapshot.hasChild(reverseChatId)){
                    chatId = reverseChatId
                    getData(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "Algo salio mal", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getData(chatId: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<MessageModel>()

                    for (show in snapshot.children) {
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    binding.recyclerView2.adapter = MessageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun storeData(msg: String) {
        val map = hashMapOf<String, String>()
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())

        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentDate"] = currentDate
        map["currentTime"] = currentTime

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
            if (it.isSuccessful) {
                binding.txtYourMessage.text = null

                getData(chatId)

                sendNotification(msg)
                Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo enviar el mensaje", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendNotification(msg: String) {
        FirebaseDatabase.getInstance().getReference("users")
            .child(receiverId!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val data = snapshot.getValue(User::class.java)

                            val notificationData = PushNotification(NotificationData("Nuevo mensaje", msg), data!!.fcmToken)
                            ApiUtilities.getInstance().sendNotification(notificationData).enqueue(object : Callback<PushNotification> {
                                override fun onResponse(
                                    call: Call<PushNotification>,
                                    response: Response<PushNotification>
                                ) {
                                    Toast.makeText(this@ChatActivity, "", Toast.LENGTH_LONG).show()
                                }

                                override fun onFailure(call: Call<PushNotification>, t: Throwable) {
                                    Toast.makeText(this@ChatActivity, "Algo salio mal", Toast.LENGTH_LONG).show()
                                }

                            })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_LONG).show()
                    }
                }
                )
    }
}