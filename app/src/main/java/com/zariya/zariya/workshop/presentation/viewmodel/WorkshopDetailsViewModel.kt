package com.zariya.zariya.workshop.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zariya.zariya.core.network.NetworkResult
import com.zariya.zariya.core.network.SingleLiveEvent
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.data.model.WorkshopLikes
import com.zariya.zariya.workshop.domain.usecase.WorkshopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WorkshopDetailsViewModel @Inject constructor(
    private val useCase: WorkshopUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiEvents = SingleLiveEvent<UIEvents>()
    val uiEvents: LiveData<UIEvents> = _uiEvents

    private val _workshopLiked = SingleLiveEvent<WorkshopLikes?>()
    val workshopLikedLiveData: LiveData<WorkshopLikes?> = _workshopLiked

    fun isWorkshopLikedByUser(workshop: Workshop? = savedStateHandle["workshop"]) {
        workshop?.workshopId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                useCase.isWorkshopLikedByUser(it).collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            withContext(Dispatchers.Main.immediate) {
                                _workshopLiked.value = it.data
                            }
                        }

                        is NetworkResult.Error -> {

                        }

                        is NetworkResult.Loading -> {}
                    }
                }
            }
        }
    }

    fun likeWorkshop(
        workshopLikes: WorkshopLikes? = workshopLikedLiveData.value,
        workshopId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (useCase.likeWorkshop(workshopLikes, workshopId)) {
                is NetworkResult.Success -> {
                    withContext(Dispatchers.Main.immediate) {
                        isWorkshopLikedByUser()
                    }
                }

                is NetworkResult.Error -> {
                    withContext(Dispatchers.Main.immediate) {
                        _uiEvents.value = UIEvents.ShowError("Something went wrong")
                    }
                }

                is NetworkResult.Loading -> {
                    withContext(Dispatchers.Main.immediate) {
                    }
                }
            }
        }
    }
}