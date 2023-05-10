package com.zariya.zariya.notification.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zariya.zariya.core.local.AppSharedPreference

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.v("FirebaseMessaging", "token $token")
        val preference = AppSharedPreference.getInstance(applicationContext)
        preference?.setFcmToken(token)
        // TODO update fcm in remote db
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.v("FirebaseMessaging", "Notification: $message")
    }
}