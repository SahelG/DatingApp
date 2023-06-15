package com.example.datingapp.Model

import java.util.Date

data class User (
    val idUser: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var phone: String? = "",
    var password: String? = "",
    var gender: String? = "",
    var birthdate: String? = "",
    var photo: String? = "",
    var fcmToken: String? = ""
)