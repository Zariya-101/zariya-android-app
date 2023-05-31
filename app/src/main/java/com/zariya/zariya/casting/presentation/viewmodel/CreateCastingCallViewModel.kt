package com.zariya.zariya.casting.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.casting.data.model.CastingCall
import com.zariya.zariya.casting.data.model.Volunteer
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateCastingCallViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private val _volunteers = SingleLiveEvent<List<Volunteer?>>()
    val volunteersLiveData: LiveData<List<Volunteer?>> = _volunteers

    var selectedVolunteer: String? = null
    var agencyId: String? = null

    fun getVolunteersForMyAgency() {
        viewModelScope.launch(Dispatchers.IO) {
            castingOnboardingUseCase.getVolunteersForMyAgency().collect {
                when (it) {
                    is NetworkResult.Success -> {
                        it.data?.data?.let {
                            if (it.isNotEmpty()) {
                                agencyId = it[0]?.worksFor
                            }
                            withContext(Dispatchers.Main.immediate) {
                                _volunteers.value = it
                            }
                        } ?: run {
                            withContext(Dispatchers.Main.immediate) {
                                _uiEvents.value = UIEvents.ShowError("Something went wrong")
                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        withContext(Dispatchers.Main.immediate) {
                            _uiEvents.value = UIEvents.ShowError("Something went wrong")
                        }
                    }

                    is NetworkResult.Loading -> {

                    }
                }
            }
        }
    }

    fun createCastingCall(castingCall: CastingCall) {
        viewModelScope.launch(Dispatchers.IO) {
            when (castingOnboardingUseCase.createCastingCall(castingCall)) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.ShowSuccess("Casting Call Created Successfully")
                    }
                    delay(1000)
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value = UIEvents.Navigate(null)
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