package com.zariya.zariya.casting.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.casting.data.model.Volunteer
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.casting.presentation.fragment.VolunteerOnboardingFragmentDirections
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VolunteerOnboardingViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    fun getUserDetails() = castingOnboardingUseCase.getUserDetails()

    fun registerVolunteer(volunteer: Volunteer) {
        viewModelScope.launch(Dispatchers.IO) {
            when (castingOnboardingUseCase.createVolunteer(volunteer)) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value =
                            UIEvents.Navigate(VolunteerOnboardingFragmentDirections.actionVolunteerProfile())
                    }
                }

                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value = UIEvents.ShowError("Something went wrong")
                    }
                }

                is NetworkResult.Loading -> {

                }
            }
        }
    }
}