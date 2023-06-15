package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class WelcomeOne : AppCompatActivity() {

    private lateinit var btnStartWO: Button
    private lateinit var txtSignInWO: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_one)

        init()

        btnStartWO.setOnClickListener{
            signUp()
        }

        txtSignInWO.setOnClickListener{
            signIn()
        }
    }


    private fun init () {
        btnStartWO = findViewById(R.id.btnStartWO)
        txtSignInWO = findViewById(R.id.txtSignInWO)
    }

    //registrarse
    private fun signUp (){
        val intent = Intent(this, SignUp::class.java).apply {}
        startActivity(intent)
    }

    //ingresar
    private fun signIn (){
        val intent = Intent(this, SignIn::class.java).apply {}
        startActivity(intent)
    }
}