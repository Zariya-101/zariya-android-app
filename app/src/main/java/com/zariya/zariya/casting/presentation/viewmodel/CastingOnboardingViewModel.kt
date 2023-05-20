package com.zariya.zariya.casting.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel

class CastingOnboardingViewModel() : ViewModel(){

    var userType: String? = null
    var userAge: String? = null
    var userComplexion: String? = null
    var userHeight: String? = null

    fun updateUserType(type: String) {
        this.userType = type
    }

    fun updateAge(age: String) {
        userAge = age
    }

    fun updateComplexion(complexion: String) {
        userComplexion = complexion
    }

    fun updateHeight(height: String) {
        userHeight = height
    }
}