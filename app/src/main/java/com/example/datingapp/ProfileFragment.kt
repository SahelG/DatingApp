package com.example.datingapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.datingapp.Model.User
import com.example.datingapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        disableFields()

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid!!).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val data = it.getValue(User::class.java)
                    binding.txtFirstNamePF.setText(data!!.firstName.toString())
                    binding.txtLastNamePF.setText(data!!.lastName.toString())
                    binding.txtPhonePF.setText(data!!.phone.toString())
                    binding.txtEmailPF.setText(data!!.email.toString())
                    binding.txtGenderPF.setText(data!!.gender.toString())
                    binding.txtBirthdayPF.setText(data!!.birthdate.toString())

                    Glide.with(requireContext()).load(data.photo).placeholder(R.drawable.ic_baseline_account_circle_24).into(binding.userImage)
                }
            }


        binding.btnLogOut.setOnClickListener {
            logOut()
        }

        return binding.root
    }

    private fun enableFields() {
        enableEditText(binding.txtFirstNamePF)
        enableEditText(binding.txtLastNamePF)
        enableEditText(binding.txtPhonePF)
        enableEditText(binding.txtEmailPF)
        enableEditText(binding.txtGenderPF)
        enableEditText(binding.txtBirthdayPF)
    }

    private fun enableEditText (editText: EditText) {
        editText.isFocusable = true
        editText.isEnabled = true
        editText.isCursorVisible = true
        editText.isFocusableInTouchMode = true
        editText.inputType = InputType.TYPE_CLASS_TEXT
    }


    private fun disableFields () {
        disableEditText(binding.txtFirstNamePF)
        disableEditText(binding.txtLastNamePF)
        disableEditText(binding.txtPhonePF)
        disableEditText(binding.txtEmailPF)
        disableEditText(binding.txtGenderPF)
        disableEditText(binding.txtBirthdayPF)
    }

    private fun disableEditText (editText: EditText) {
        editText.isFocusable = false
        editText.isEnabled = false
        editText.isCursorVisible = false
    }

    private fun logOut () {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(requireContext(), WelcomeOne::class.java))
        requireActivity().finish()
    }

    private fun editUser (user: User) {
        var database = Firebase.database.reference
        var auth = Firebase.auth.currentUser

        database.child("users").child(user.idUser.toString()).setValue(user)

        auth?.updateEmail(user.email.toString())?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Guardado", "Guardado")
            }
        }
    }

}