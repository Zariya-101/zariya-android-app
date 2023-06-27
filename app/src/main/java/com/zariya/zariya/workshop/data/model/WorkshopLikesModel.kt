package com.zariya.zariya.workshop.data.model

import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.IgnoredOnParcel

data class WorkshopLikes(
    val userId: String,
    val workshopId: String
) {
    @IgnoredOnParcel
    @get:Exclude
    var mappingId: String? = ""
}