package com.zariya.zariya.casting.presentation.viewmodel

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CastingOnboardingViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase
) : ViewModel() {

    var userType: String? = null

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    var actorProfileDetails: ActorProfile = ActorProfile()

    fun updateActorProfileDetails(actorProfile: ActorProfile) {
        actorProfileDetails = actorProfile
    }

    fun updateUserType(type: String) {
        this.userType = type
    }

    fun createActorProfile(actorProfile: ActorProfile = actorProfileDetails) {
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
}