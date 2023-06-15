package com.example.datingapp.Api

import android.app.Notification
import com.example.datingapp.Model.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers("Content-Type:application/json", "Authorization:key=AAAAdLx4FtM:APA91bGcrFwmUQCzVqxrT32m6JWlYlLvq4pPTxqn5vFGl_F0PA1dpcMXUz8KS4lGUuFZ4cMjK8IVpD9aD2EHCN_PXeqguEy3ihVdMaRFQs1FyotntmVciOCqSfIgCffIkMHt1dS0ns9I")
    @POST("fcm/send")

    fun sendNotification(@Body notification: PushNotification) : Call<PushNotification>

}