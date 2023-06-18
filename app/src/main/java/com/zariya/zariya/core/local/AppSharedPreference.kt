package com.zariya.zariya.core.local

import android.content.Context
import android.content.SharedPreferences
import com.zariya.zariya.auth.data.model.User

class AppSharedPreference private constructor(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        "zariya_pref", Context.MODE_MULTI_PROCESS
    )

    companion object {
        private var instance: AppSharedPreference? = null
        fun getInstance(context: Context): AppSharedPreference? {
            if (instance == null) {
                synchronized(AppSharedPreference::class.java) {
                    if (instance == null) instance =
                        AppSharedPreference(context)
                }
            }
            return instance
        }
    }

    fun setFcmToken(token: String) {
        sharedPref.edit().putString("fcmToken", token).apply()
    }

    fun getFcmToken() = sharedPref.getString("fcmToken", "")

    fun resetLoginData() {
        sharedPref.edit().clear().apply()
    }

    fun setUserData(user: User) {
        sharedPref.edit()
            .putString("name", user.name)
            .putString("phone", user.phone)
            .putString("email", user.email)
            .putString("fcmToken", user.fcmToken)
            .putString("dob", user.dob)
            .putString("countryCode", user.countryCode)
            .putString("userId", user.id)
            .putString("profilePic", user.profilePic)
            .putString("coverPic", user.coverPic)
            .putString("role", user.role)
            .putString("createdAt", user.createdAt)
            .putString("gender", user.gender)
            .apply()
    }

    fun getUserData(): User = User(
        name = sharedPref.getString("name", ""),
        phone = sharedPref.getString("phone", ""),
        email = sharedPref.getString("email", ""),
        fcmToken = sharedPref.getString("fcmToken", ""),
        dob = sharedPref.getString("dob", ""),
        countryCode = sharedPref.getString("countryCode", ""),
        id = sharedPref.getString("userId", ""),
        profilePic = sharedPref.getString("profilePic", ""),
        coverPic = sharedPref.getString("coverPic", ""),
        role = sharedPref.getString("role", ""),
        createdAt = sharedPref.getString("createdAt", ""),
        gender = sharedPref.getString("gender", ""),
    )
}