package com.zariya.zariya.casting.data.model

data class Agency(
    var userId: String = "",
    val name: String = "",
    var profilePic: String = "",
    val startedIn: String = "",
    val email: String = "",
    val phone: String = "",
    val description: String = "",
    val speciality: ArrayList<String> = arrayListOf(),
    val knownFor: String = ""
)