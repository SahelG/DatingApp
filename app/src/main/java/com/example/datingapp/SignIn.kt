package com.example.datingapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignIn : AppCompatActivity() {

    private lateinit var txtEmailSI: EditText
    private lateinit var txtPasswordSI: EditText

    private lateinit var txtFortgotPasswordSI: TextView
    private lateinit var txtSignUPSI: TextView

    private lateinit var btnSignInSI: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()

        btnSignInSI.setOnClickListener {
            if(ValidateData.Validate(arrayListOf(txtEmailSI, txtPasswordSI)))
                signIn(txtEmailSI.text.toString(), txtPasswordSI.text.toString())
        }

        txtFortgotPasswordSI.setOnClickListener{
            if(ValidateData.Validate(arrayListOf(txtEmailSI))) {
                val emailAddress = txtEmailSI.text.toString()

                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@SignIn, "Email sent", Toast.LENGTH_LONG).show()
                            Log.d(ContentValues.TAG, "Email sent.")
                        }else Toast.makeText(this@SignIn, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
            }
        }

        txtSignUPSI.setOnClickListener {
            val intent = Intent(this, SignUp::class.java).apply {}
            startActivity(intent)
            finish()
        }

    }

    private fun init () {
        txtEmailSI = findViewById(R.id.txtEmailSI)
        txtPasswordSI = findViewById(R.id.txtPasswordSI)
        txtFortgotPasswordSI = findViewById(R.id.txtFortgotPasswordSI)
        txtSignUPSI = findViewById(R.id.txtSignUPSI)

        btnSignInSI = findViewById(R.id.btnSignInSI)
    }

    private fun signIn (email: String, password: String) {
        var firebaseAuth = Firebase.auth

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                task -> if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(baseContext, user?.uid.toString(), Toast.LENGTH_LONG).show()

                    val intent = Intent(this, HomeScreen::class.java).apply {}
                    startActivity(intent)
                    finish()
            }else Toast.makeText(baseContext, task.exception?.message, Toast.LENGTH_LONG).show()
        }
    }
}