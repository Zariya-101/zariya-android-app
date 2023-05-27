package com.zariya.zariya.profile.presentation

import androidx.lifecycle.ViewModel
import com.zariya.zariya.core.local.AppSharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preference: AppSharedPreference?
): ViewModel() {

    fun getUser() = preference?.getUserData()
}