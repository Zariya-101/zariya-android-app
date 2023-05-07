package com.zariya.zariya

import android.app.Application
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration

//const val AppId = "application-0-lsfzx"
const val AppId = "zariya-android-application-fuiuj"

lateinit var app: App

class ZariyaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        app = App.create(
            AppConfiguration.Builder(getString(R.string.realm_app_id))
                .baseUrl(getString(R.string.realm_base_url))
                .build()
        )
    }
}