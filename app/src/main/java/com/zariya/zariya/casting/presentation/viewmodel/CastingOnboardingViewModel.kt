package com.zariya.zariya.casting.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.domain.usecase.AuthUseCase
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.casting.presentation.fragment.CastingOnboardingFragmentDirections
import com.zariya.zariya.core.local.AppSharedPreference
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.upload.domain.usecase.UploadUseCase
import com.zariya.zariya.utils.ACTOR
import com.zariya.zariya.utils.AGENCY
import com.zariya.zariya.utils.VOLUNTEER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CastingOnboardingViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase,
    private val uploadUseCase: UploadUseCase,
    private val authUseCase: AuthUseCase,
    private val preference: AppSharedPreference?
) : ViewModel() {

    var userType: String? = null

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    var actorProfileDetails: ActorProfile = ActorProfile()

    private val _getActorProfileDetails = SingleLiveEvent<ActorProfile>()
    val getActorProfileDetails: LiveData<ActorProfile> = _getActorProfileDetails

    fun updateActorProfileDetails(actorProfile: ActorProfile) {
        actorProfileDetails = actorProfile
    }

    fun updateUserType(type: String) {
        this.userType = type
    }

    fun uploadImage(imageUri: Uri, imageType: String, onUploaded: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = uploadUseCase.uploadImage(imageUri, imageType)) {
                is NetworkResult.Success -> {
                    Log.v("CastingOnboardingVM", "Upload Image Success")
                    result.data?.let { uri ->
                        actorProfileDetails.imageList.add(0, uri.toString())
                        if (actorProfileDetails.imageList.size > 5) {
                            actorProfileDetails.imageList.remove("")
                        }
                    }
                    withContext(Dispatchers.Main.immediate) {
                        onUploaded()
                    }
                }

                is NetworkResult.Error -> {
                    Log.e("CastingOnboardingVM", "Upload Image Failed")
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    fun createActorProfile(actorProfile: ActorProfile = actorProfileDetails) {
        if (actorProfile.imageList.contains("")) {
            actorProfile.imageList.remove("")
        }
        viewModelScope.launch(Dispatchers.IO) {
            when (castingOnboardingUseCase.createActorProfile(actorProfile)) {
                is NetworkResult.Success -> {
                    Log.v("CastingOnboardingVM", "Create Actor Profile Success")
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value =
                            UIEvents.Navigate(CastingOnboardingFragmentDirections.actionActorProfile())
                    }
                }

                is NetworkResult.Error -> {
                    Log.e("CastingOnboardingVM", "Create Actor Profile Failed")
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }

    fun getActorProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            castingOnboardingUseCase.getActorProfile().collect {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.v("CastingOnboardingVM", "Get Actor Profile Success")
                        withContext(Dispatchers.Main.immediate) {
                            it.data?.let { actor ->
                                _getActorProfileDetails.value = actor
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("CastingOnboardingVM", "Get Actor Profile Failed")
                    }

                    is NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    fun fetchUserDetailsFromRemote() {
        getUserDetails()?.let { user ->
            viewModelScope.launch(Dispatchers.IO) {
                authUseCase.getUserFromDB(user).collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.data?.let { user ->
                                preference?.setUserData(user)
                                withContext(Dispatchers.Main.immediate) {
                                    when (user.role) {
                                        ACTOR -> {
                                            _uiEvents.value = UIEvents.Loading(false)
                                            _uiEvents.value = UIEvents.Navigate(
                                                CastingOnboardingFragmentDirections.actionActorProfile()
                                            )
                                        }

                                        AGENCY -> {
                                            _uiEvents.value = UIEvents.Loading(false)
                                            _uiEvents.value = UIEvents.Navigate(
                                                CastingOnboardingFragmentDirections.actionCastingAgency()
                                            )
                                        }

                                        VOLUNTEER -> {
                                            _uiEvents.value = UIEvents.Loading(false)
                                            _uiEvents.value = UIEvents.Navigate(
                                                CastingOnboardingFragmentDirections.actionVolunteerCastingFeeds()
                                            )
                                        }

                                        else -> {
                                            _uiEvents.value = UIEvents.Loading(false)
                                            _uiEvents.value = UIEvents.RefreshUi("")
                                        }
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
    }

    fun getUserDetails() = castingOnboardingUseCase.getUserDetails()
}