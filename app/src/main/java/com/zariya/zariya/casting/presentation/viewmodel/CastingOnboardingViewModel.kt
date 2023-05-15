package com.zariya.zariya.casting.presentation.viewmodel

import android.app.Application
import com.zariya.zariya.core.ui.BaseViewModel

class CastingOnboardingViewModel(private val application: Application) :
    BaseViewModel(application) {

    var userType: String? = null
    var userAge: String? = null
    var userComplexion: String? = null

    fun updateUserType(type: String) {
        this.userType = type
    }

    fun updateAge(age: String) {
        userAge = age
    }

    fun updateComplexion(complexion: String) {
        userComplexion = complexion
    }
}