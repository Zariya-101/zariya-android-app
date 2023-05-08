package com.zariya.zariya.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.auth.data.model.Customers
import com.zariya.zariya.auth.data.repository.AuthRepositoryImpl
import com.zariya.zariya.remote.SyncRepositoryImpl
import com.zariya.zariya.auth.domain.repository.AuthRepository
import com.zariya.zariya.remote.SyncRepository
import com.zariya.zariya.auth.presentation.fragment.LoginFragmentDirections
import com.zariya.zariya.auth.presentation.fragment.SignUpFragmentDirections
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {

    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private lateinit var syncRepository: SyncRepository

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private var isNewUser: Boolean = false

    fun register(customer: Customers) {
        _uiEvents.value = UIEvents.Loading(true)
        viewModelScope.launch(Dispatchers.IO) {
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
        syncRepository = SyncRepositoryImpl()
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                syncRepository.createCustomer(customer)
            }.onSuccess {
                Log.v("AuthViewModel", "Customer Creation Success")
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