package com.zariya.zariya.workshop.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.domain.usecase.WorkshopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WorkshopListViewModel @Inject constructor(
    private val useCase: WorkshopUseCase
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private val _workshops = SingleLiveEvent<List<Workshop?>>()
    val workshopsLiveData: LiveData<List<Workshop?>> = _workshops

    fun getWorkshopsList() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getWorkshopsList().collect {
                when (it) {
                    is NetworkResult.Success -> {
                        withContext(Dispatchers.Main.immediate) {
                            it.data?.let { workshops ->
                                _workshops.value = workshops
                            } ?: run {
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