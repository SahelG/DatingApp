package com.example.datingapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64.DEFAULT
import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.datingapp.databinding.ActivitySignUp2Binding
import com.example.datingapp.databinding.ItemUserLayoutBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import com.example.datingapp.Model.User as ModelUser

class SignUp_2 : AppCompatActivity() {

    private lateinit var txtDaySU: EditText
    private lateinit var txtMonthSU: EditText
    private lateinit var txtYearSU: EditText
    private lateinit var btnSignUpSU: Button

    //image
    private lateinit var image: ImageView
    private lateinit var image1: ImageView
    private var uri: Uri? = null

    //card
    private lateinit var card1: CardView

    private lateinit var db: DatabaseReference
    var sImage: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        init()

        btnSignUpSU.setOnClickListener {
            if (ValidateData.Validate(arrayListOf(txtDaySU, txtMonthSU, txtYearSU))) {
                val txtDaySUText = txtDaySU.text.toString()
                val txtMonthSUText = txtMonthSU.text.toString()
                val txtYearSUText = txtYearSU.text.toString()

                if (isDayValid(txtDaySUText) && isMothValid(txtMonthSUText) && isYearValid(txtYearSUText)) {
                    //if (image1.drawable == null)
                    if (uri != null)
                        storeData()
                    else
                        Toast.makeText(this@SignUp_2, "Seleccione una foto", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this@SignUp_2, "Fecha invalida", Toast.LENGTH_LONG).show()
                }
            }

        }

        card1.setOnClickListener {
            image = findViewById(R.id.img1)
            uploadImage(image)
        }

    }

    private fun init () {
        txtDaySU = findViewById(R.id.txtDaySU)
        txtMonthSU = findViewById(R.id.txtMonthSU)
        txtYearSU = findViewById(R.id.txtYearSU)
        btnSignUpSU = findViewById(R.id.btnSignUpSU)

        card1 = findViewById(R.id.cardView1)
        image1 = findViewById(R.id.img1)

        val textView = findViewById<TextView>(R.id.textView7)
        val mensaje = SpannableString("Completa tu perfil")
        val colorSpan = ForegroundColorSpan(Color.parseColor("#6D6DA8"))
        mensaje.setSpan(colorSpan, 12, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        textView.text = mensaje
    }

    private fun isDayValid (day: String): Boolean {
        var daysInMonth: Int = 0
        if (day.matches(Regex("[1-9]\\d*"))) {
            daysInMonth = when (Calendar.getInstance().get(Calendar.MONTH)) {
                Calendar.FEBRUARY -> 28
                Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
                else -> 31
            }
        }
        return day.toIntOrNull()?.let { it in 1..daysInMonth } ?: false
    }

    private fun isMothValid (month: String): Boolean {
        val regex = "^(1[0-2]|[1-9])\$".toRegex()
        return regex.matches(month)
    }

    private fun isYearValid (yearString: String): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        if (yearString.matches("[1-9]\\d{3}".toRegex())) {
            val year = yearString.toInt()
            if (year in 1950..currentYear) {
                return true
            } else {
                return false
            }
        } else {
            return false
        }

    }



    private fun uploadImage (image: ImageView) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            uri = data?.data!!
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                image1.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_LONG).show()
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
            image.setImageURI(uri)
        }
    }

    private fun SaveUser (user: ModelUser) {
        var database = Firebase.database.reference
        //nodos dentro de otro nodo
        database.child("users").child(user.idUser!!).setValue(user)
    }

    private fun storeData() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener (OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            //obtener un nuevo token
            val token = task.result

            var auth = Firebase.auth
            var storageReference: StorageReference

            //var uid = Firebase.auth.currentUser?.uid

            //val idUser = uid.toString()
            //val idUser = UUID.randomUUID().toString()
            val txtFirstNameSU = intent.getStringExtra("txtFirstNameSU")
            val txtLastNameSU = intent.getStringExtra("txtLastNameSU")
            val txtEmailSU = intent.getStringExtra("txtEmailSU")

            val txtPasswordSU = intent.getStringExtra("txtPasswordSU")
            val txtPhoneSU = intent.getStringExtra("txtPhoneSU")
            val txtGenderSU = intent.getStringExtra("txtGenderSU")
            val txtBirthdateSU = txtDaySU.text.toString() + "/" + txtMonthSU.text.toString() + "/" + txtYearSU.text.toString()
            //val photoSU = uri.toString()
            val fcmToken = token

            //val data = ModelUser(idUser, txtFirstNameSU, txtLastNameSU, txtEmailSU, txtPhoneSU, txtPasswordSU, txtGenderSU, txtBirthdateSU, photoSU)


            auth.createUserWithEmailAndPassword(txtEmailSU.toString(), txtPasswordSU.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //SaveUser(data)

                        storageReference = FirebaseStorage.getInstance().getReference("users/" + auth.currentUser?.uid)
                        uri?.let {
                            storageReference.putFile(it).addOnSuccessListener {
                                storageReference.downloadUrl.addOnSuccessListener { uri ->
                                    var uid = Firebase.auth.currentUser?.uid
                                    val idUser = uid.toString()
                                    val downloadUrl2 = uri.toString()
                                    Log.d("ÑAÑÑAÑAÑA", downloadUrl2)
                                    val data = ModelUser(idUser, txtFirstNameSU, txtLastNameSU, txtEmailSU, txtPhoneSU, txtPasswordSU, txtGenderSU, txtBirthdateSU, downloadUrl2, fcmToken)

                                    SaveUser(data)
                                    Toast.makeText(this, "", Toast.LENGTH_LONG).show()
                                }
                                Toast.makeText(this, "", Toast.LENGTH_LONG).show()
                            }.addOnFailureListener{
                                Toast.makeText(this, "", Toast.LENGTH_LONG).show()

                            }
                        }

                        Toast.makeText(this, "Usuario creado", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@SignUp_2, HomeScreen::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(baseContext, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        })
    }

}