package com.example.datingapp.Model

data class PushNotification (
    val data: NotificationData,
    val to: String? = ""
)