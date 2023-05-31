package com.zariya.zariya.casting.data.model

data class CastingCall(
    var agencyId: String = "",
    val gender: String = "",
    val role: String = "",
    val ageRange: String = "",
    val complexion: String = "",
    val paymentType: String = "",
    val payment: String? = "",
    val description: String = "",
    val assignedTo: String = "",
    var image: String? = null
)