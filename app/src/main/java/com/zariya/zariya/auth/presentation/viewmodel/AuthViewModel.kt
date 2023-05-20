package com.zariya.zariya.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.zariya.zariya.app
import com.zariya.zariya.auth.data.model.Customers
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.remote.SyncRepositoryImpl
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.remote.SyncRepository
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

    private lateinit var syncRepository: SyncRepository

    private val _authenticateWithPhone = SingleLiveEvent<User>()
    val authenticateWithPhoneLiveData: LiveData<User> = _authenticateWithPhone

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private var isNewUser: Boolean = false

    fun authenticateWithPhone(credential: PhoneAuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.authenticateWithPhone(credential).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.let { user ->
//                            _authenticateWithPhone.value = user
                            if (user.isNew!!) {
                                withContext(Dispatchers.Main.immediate) {
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

    fun isUserLoggedIn(): Boolean {
        return preference?.getUserData()?.id.isNullOrEmpty().not()
    }

    fun register(customer: Customers) {
        _uiEvents.value = UIEvents.Loading(true)
        viewModelScope.launch(Dispatchers.IO) {
            Log.v("AuthViewModel", "Register Start")
            kotlin.runCatching {
                authRepository.createAccount(
                    email = customer.phone.plus("@zariya.com"),
                    password = customer.phone
                )
            }.onSuccess {
                Log.v("AuthViewModel", "Register Success")
                login(customer.phone, customer = customer)
            }.onFailure {
                Log.e("AuthViewModel", "Register Failed: $it")
                withContext(Dispatchers.Main) {
                    _uiEvents.value = UIEvents.ShowError(it.message)
                    _uiEvents.value = UIEvents.Loading(false)
                }
            }
        }
    }

    fun login(mobile: String, customer: Customers?) {
        isNewUser = customer != null
        if (!isNewUser) {
            _uiEvents.value = UIEvents.Loading(true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                Log.v("AuthViewModel", "Login Start")
                authRepository.login(email = mobile.plus("@zariya.com"), password = mobile)
            }.onSuccess {
                Log.v("AuthViewModel", "Login Success")
                if (isNewUser) {
                    if (customer != null) {
                        createCustomer(customer = customer)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value =
                            UIEvents.Navigate(LoginFragmentDirections.actionLoginToHome())
                    }
                }
            }.onFailure {
                Log.e("AuthViewModel", "Login Failed: $it")
                withContext(Dispatchers.Main) {
                    _uiEvents.value = UIEvents.ShowError(it.message)
                    _uiEvents.value = UIEvents.Loading(false)
                }
            }
        }
    }

    fun createCustomer(customer: Customers) {
        customer.fcmToken = preference?.getFcmToken() ?: ""
        syncRepository = SyncRepositoryImpl()
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                Log.v("AuthViewModel", "Customer Creation Start")
                syncRepository.createCustomer(customer)
            }.onSuccess {
                Log.v("AuthViewModel", "Customer Creation Success")
                customer.owner_id = app.currentUser?.id ?: ""
                preference?.setCustomerData(customer)
                withContext(Dispatchers.Main) {
                    _uiEvents.value = UIEvents.Loading(false)
                    _uiEvents.value =
                        UIEvents.Navigate(SignUpFragmentDirections.actionSignUpToHome())
                }
            }.onFailure {
                Log.e("AuthViewModel", "Customer Creation Failed: $it")
                withContext(Dispatchers.Main) {
                    _uiEvents.value = UIEvents.ShowError(it.message)
                    _uiEvents.value = UIEvents.Loading(false)
                }
            }
        }
    }
}