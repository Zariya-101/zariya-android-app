package com.zariya.zariya.casting.data.model

import com.google.firebase.firestore.Exclude

data class Volunteer(
    var userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val speciality: ArrayList<String> = arrayListOf(),
    val experience: String = "",
    val worksFor: String = ""
) {
    @get:Exclude
    var volunteerId: String? = ""
}