package com.zariya.zariya.casting.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.casting.presentation.fragment.AgencyDetailsFormFragmentDirections
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.upload.domain.usecase.UploadUseCase
import com.zariya.zariya.utils.AGENCY_PROFILE_IMAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AgencyDetailsFormViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase,
    private val uploadUseCase: UploadUseCase
) : ViewModel() {

    var imageUri: Uri? = null

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    fun getUserDetails() = castingOnboardingUseCase.getUserDetails()

    fun registerAgency(agency: Agency) {
        viewModelScope.launch(Dispatchers.IO) {
            imageUri?.let {
                when (val result = uploadUseCase.uploadImage(it, AGENCY_PROFILE_IMAGE)) {
                    is NetworkResult.Success -> {
                        result.data?.let { uri ->
                            agency.profilePic = uri.toString()
                            createAgency(agency)
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

                    }
                }
            }
        }
    }

    private fun createAgency(agency: Agency) {
        viewModelScope.launch(Dispatchers.IO) {
            when (castingOnboardingUseCase.createAgency(agency)) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.Loading(false)
                        _uiEvents.value =
                            UIEvents.Navigate(AgencyDetailsFormFragmentDirections.actionAgencyProfile())
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