package com.zariya.zariya.core.local

import android.content.Context
import android.content.SharedPreferences
import com.zariya.zariya.auth.data.model.Customers

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

    fun setCustomerData(customer: Customers) {
        sharedPref.edit()
            .putString("name", customer.name)
            .putString("phone", customer.phone)
            .putString("fcmToken", customer.fcmToken)
            .putString("dob", customer.dob)
            .putString("countryCode", customer.countryCode)
            .putString("customerId", customer._id.toString())
            .putString("userId", customer.owner_id)
            .apply()
    }

    fun getCustomerData(): Customers = Customers().apply {
        name = sharedPref.getString("name", "") ?: ""
        phone = sharedPref.getString("phone", "") ?: ""
        fcmToken = sharedPref.getString("fcmToken", "") ?: ""
        dob = sharedPref.getString("dob", "") ?: ""
        countryCode = sharedPref.getString("countryCode", "") ?: ""
    }
}