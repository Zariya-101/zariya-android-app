package com.zariya.zariya.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.PhoneAuthCredential
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.domain.usecase.AuthUseCase
import com.zariya.zariya.auth.presentation.fragment.LocationFragmentDirections
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
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private val _currentCity = SingleLiveEvent<String?>()
    val currentCityLiveData: LiveData<String?> = _currentCity

    var selectedGender: String? = null

    init {
        updateCurrentCity(null)
    }

    fun updateCurrentCity(city: String?) {
        _currentCity.value = city
    }

    fun authenticateWithPhone(credential: PhoneAuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.authenticateWithPhone(credential).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            if (user.isNew!!) {
                                withContext(Dispatchers.Main.immediate) {
                                    user.countryCode = user.phone?.substring(0, 3)
                                    user.phone = user.phone?.substring(3, 13)
                                    val action = LoginFragmentDirections.actionLoginToSignup()
                                    action.user = user
                                    _uiEvents.value = UIEvents.Loading(false)
                                    _uiEvents.value = UIEvents.Navigate(action)
                                }
                            } else {
                                updateFcmToken(user, true)
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.Loading(false)
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(false)
                            _uiEvents.value = UIEvents.ShowError("Something went wrong")
                        }
                    }

                    is NetworkResult.Loading -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(true)
                        }
                    }
                }
            }
        }
    }

    fun authenticateWithGoogle(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.authenticateWithGoogle(credential).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            if (user.isNew!!) {
                                withContext(Dispatchers.Main.immediate) {
                                    _uiEvents.value = UIEvents.Loading(false)
                                    val action = LoginFragmentDirections.actionLoginToSignup()
                                    action.user = user
                                    _uiEvents.value = UIEvents.Navigate(action)
                                }
                            } else {
                                updateFcmToken(user, true)
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.Loading(false)
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(false)
                            _uiEvents.value = UIEvents.ShowError("Something went wrong")
                        }
                    }

                    is NetworkResult.Loading -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(true)
                        }
                    }
                }
            }
        }
    }

    fun authenticateWithFacebook(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.authenticateWithFacebook(credential).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            if (user.isNew!!) {
                                withContext(Dispatchers.Main.immediate) {
                                    _uiEvents.value = UIEvents.Loading(false)
                                    val action = LoginFragmentDirections.actionLoginToSignup()
                                    action.user = user
                                    _uiEvents.value = UIEvents.Navigate(action)
                                }
                            } else {
                                updateFcmToken(user, true)
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.Loading(false)
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(false)
                            _uiEvents.value = UIEvents.ShowError("Something went wrong")
                        }
                    }

                    is NetworkResult.Loading -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(true)
                        }
                    }
                }
            }
        }
    }

    fun updateFcmToken(user: User, isLogin: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            when (authUseCase.updateFcmToken(user)) {
                is NetworkResult.Success -> {
                    fetchUser(user, isLogin)
                }

                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value = UIEvents.ShowError("Something went wrong")
                    }
                }

                is NetworkResult.Loading -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(true)
                    }
                }
            }
        }
    }

    fun updateUserLocation(location: String) {
        _uiEvents.value = UIEvents.Loading(true)
        viewModelScope.launch {
            when (authUseCase.updateUserLocation(location)) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value =
                            UIEvents.Navigate(LocationFragmentDirections.actionLocationToHome())
                    }
                }

                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value = UIEvents.ShowError("Something went wrong")
                    }
                }

                is NetworkResult.Loading -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(true)
                    }
                }
            }
        }
    }

    fun fetchUser(authenticatedUser: User, isLogin: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.getUserFromDB(authenticatedUser).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
                            preference?.setUserData(user)
                            if (isLogin) {
                                withContext(Dispatchers.Main.immediate) {
                                    _uiEvents.value = UIEvents.Loading(false)
                                    _uiEvents.value =
                                        UIEvents.Navigate(LoginFragmentDirections.actionLoginToHome())
                                }
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.Loading(false)
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(false)
                            _uiEvents.value = UIEvents.ShowError("Something went wrong")
                        }
                    }

                    is NetworkResult.Loading -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.Loading(true)
                        }
                    }
                }
            }
        }
    }

    fun signUpUser(authenticatedUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            when (authUseCase.createUser(authenticatedUser)) {
                is NetworkResult.Success -> {
                    preference?.setUserData(authenticatedUser)
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value =
                            UIEvents.Navigate(SignUpFragmentDirections.actionSignUpToLocation())
                    }
                }

                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value = UIEvents.ShowError("Something went wrong")
                    }
                }

                is NetworkResult.Loading -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(true)
                    }
                }
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return preference?.getUserData()?.id.isNullOrEmpty().not()
    }
}