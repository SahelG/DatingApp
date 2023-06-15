
package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.MimeTypeFilter.matches
import com.example.datingapp.Model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignUp : AppCompatActivity() {

    private lateinit var txtFirstNameSU: EditText
    private lateinit var txtLastNameSU: EditText
    private lateinit var txtEmailSU: EditText
    private lateinit var txtPasswordSU: EditText
    private lateinit var txtPhoneSU: EditText

    //botones
    private lateinit var btnFloatingNext: FloatingActionButton
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioBtnText: RadioButton

    //
    private val regex = Regex("^\\d{10}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()

        btnFloatingNext.setOnClickListener {

            if (ValidateData.Validate(arrayListOf(txtFirstNameSU, txtLastNameSU, txtEmailSU, txtPasswordSU, txtPhoneSU))) {
                val txtPhoneSUText = txtPhoneSU.text.toString()
                val regex = Regex("^\\d{10}$")

                if (txtPhoneSUText.matches(regex)) {
                    if (radioValidation() != "null") {
                        sendData()
                    }

                } else {
                    Toast.makeText(this@SignUp, "El numero de telefono debe tener 10 digitos", Toast.LENGTH_LONG).show()
                }

            }


            //radioValidation()
            //sendData()
        }

    }

    private fun init () {
        txtFirstNameSU = findViewById(R.id.txtFirstNameSU)
        txtLastNameSU = findViewById(R.id.txtLastNameSU)
        txtEmailSU = findViewById(R.id.txtEmailSU)
        txtPasswordSU = findViewById(R.id.txtPasswordSU)
        txtPhoneSU = findViewById(R.id.txtPhoneSU)

        btnFloatingNext = findViewById(R.id.btnFloatingNext)
        radioGroupGender = findViewById(R.id.radioGroup)
    }

    private fun sendData () {
        val txtGender = radioValidation()

        val intent = Intent(this, SignUp_2::class.java).apply {
            putExtra("txtFirstNameSU", txtFirstNameSU.text.toString())
            putExtra("txtLastNameSU", txtLastNameSU.text.toString())
            putExtra("txtEmailSU", txtEmailSU.text.toString())
            putExtra("txtPasswordSU", txtPasswordSU.text.toString())
            putExtra("txtPhoneSU", txtPhoneSU.text.toString())
            putExtra("txtGenderSU", txtGender)
        }

        startActivity(intent)
    }

    private fun radioValidation (): String {
        val selectedButtonId = radioGroupGender.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedButtonId)

        if (selectedButtonId == -1) {
            Toast.makeText(this, "Favor de ingresar su genero", Toast.LENGTH_LONG).show()
            return "null"
        }else {

            radioBtnText = findViewById(selectedButtonId)

            when (selectedRadioButton.id) {
                R.id.rbtWomanSU -> {
                    return selectedRadioButton.text.toString()
                }
                R.id.rbtMenSU -> {
                    return selectedRadioButton.text.toString()
                }
                R.id.rbtNoSU -> {
                    return selectedRadioButton.text.toString()
                }
            }
        }
        return "null"
    }
}