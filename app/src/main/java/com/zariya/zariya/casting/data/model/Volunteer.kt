package com.zariya.zariya.casting.data.model

data class Volunteer(
    var userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val speciality: ArrayList<String> = arrayListOf(),
    val experience: String = "",
    val worksFor: String = ""
)