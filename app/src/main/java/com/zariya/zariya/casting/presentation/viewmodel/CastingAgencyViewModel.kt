package com.zariya.zariya.casting.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.domain.usecase.CastingOnboardingUseCase
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CastingAgencyViewModel @Inject constructor(
    private val castingOnboardingUseCase: CastingOnboardingUseCase
) : ViewModel() {

    private val _actors = SingleLiveEvent<List<ActorProfile?>>()
    val actorsLiveData: LiveData<List<ActorProfile?>> = _actors

    fun getActors() {
        viewModelScope.launch(Dispatchers.IO) {
            castingOnboardingUseCase.getActors().collect {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.v("CastingAgencyVM", "Get Actors Success")
                        withContext(Dispatchers.Main.immediate) {
                            it.data?.let { actors ->
                                _actors.value = actors
                            } ?: run {

                            }
                        }
                    }

                    is NetworkResult.Error -> {
                        Log.e("CastingAgencyVM", "Get Actors Failed")
                    }

                    is NetworkResult.Loading -> {
                    }
                }
            }
        }
    }
}