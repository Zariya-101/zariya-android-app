package com.zariya.zariya.profile.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.profile.domain.usecase.ProfileUseCase
import com.zariya.zariya.upload.domain.usecase.UploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val uploadUseCase: UploadUseCase
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private val _user = SingleLiveEvent<User>()
    val user: LiveData<User> = _user

    init {
        getUser()
    }

    fun getUser() {
        _user.value = profileUseCase.getUser()
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            when (profileUseCase.updateUser(user)) {
                is NetworkResult.Success -> {
                    profileUseCase.updateStoredUser(user)
                    withContext(Dispatchers.Main.immediate) {
                        _user.value = user
                        _uiEvents.value = UIEvents.Loading(false)
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

    fun uploadImage(imageUri: Uri, imageType: String, onUploaded: (Uri) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = uploadUseCase.uploadImage(imageUri, imageType)) {
                is NetworkResult.Success -> {
                    result.data?.let { uri ->
                        withContext(Dispatchers.Main.immediate) {
                            onUploaded(uri)
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