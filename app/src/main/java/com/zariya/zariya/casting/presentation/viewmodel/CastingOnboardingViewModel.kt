package com.zariya.zariya.casting.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.casting.presentation.fragment.CastingOnboardingFragmentDirections
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.upload.domain.usecase.UploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CastingOnboardingViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase,
    private val uploadUseCase: UploadUseCase
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

    fun uploadImage(imageUri: Uri, onUploaded: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = uploadUseCase.uploadImage(imageUri)
            when (result) {
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

    fun getUserDetails() = castingOnboardingUseCase.getUserDetails()
}