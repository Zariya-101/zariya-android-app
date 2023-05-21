package com.zariya.zariya.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.auth.presentation.fragment.LoginFragmentDirections
import com.zariya.zariya.auth.presentation.fragment.SignUpFragmentDirections
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preference: AppSharedPreference?,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    fun authenticateWithPhone(credential: PhoneAuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.authenticateWithPhone(credential).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            if (user.isNew!!) {
                                withContext(Dispatchers.Main.immediate) {
                                    user.countryCode = user.phone?.substring(0, 3)
                                    user.phone = user.phone?.substring(3, 13)
                                    val action = LoginFragmentDirections.actionLoginToSignup()
                                    action.user = user
                                    _uiEvents.value = UIEvents.Navigate(action)
                                }
                            } else {
                                updateFcmToken(user, true)
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("AuthViewModel", "Auth with Phone Failed: $it")
                    }

                    is NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    fun updateFcmToken(user: User, isLogin: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            when (authRepository.updateFcmToken(user)) {
                is NetworkResult.Success -> {
                    fetchUser(user, isLogin)
                }

                is NetworkResult.Error -> {
                    Log.e("AuthViewModel", "Update FCM Token Failed")
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    fun fetchUser(authenticatedUser: User, isLogin: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getUserFromDB(authenticatedUser).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            preference?.setUserData(user)
                            if (isLogin) {
                                withContext(Dispatchers.Main.immediate) {
                                    _uiEvents.value =
                                        UIEvents.Navigate(LoginFragmentDirections.actionLoginToHome())
                                }
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("AuthViewModel", "fetch User Failed: $it")
                    }

                    is NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    fun signUpUser(authenticatedUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            when (authRepository.signUpUser(authenticatedUser)) {
                is NetworkResult.Success -> {
                    preference?.setUserData(authenticatedUser)
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value =
                            UIEvents.Navigate(SignUpFragmentDirections.actionSignUpToHome())
                    }
                }

                is NetworkResult.Error -> {
                    Log.e("AuthViewModel", "Signup user Failed")
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return preference?.getUserData()?.id.isNullOrEmpty().not()
    }
}