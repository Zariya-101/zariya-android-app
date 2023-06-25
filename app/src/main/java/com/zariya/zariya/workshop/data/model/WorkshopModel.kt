package com.zariya.zariya.workshop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workshop(
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val category: String = "",
    val discountedPrice: Float = 0f,
    val originalPrice: Float = 0f,
    val displayImage: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val enrolledUsers: List<String> = emptyList(),
    val likes: Int = 0,
    val reviews: List<String> = emptyList(),
    val rating: Float = 0f
) : Parcelable