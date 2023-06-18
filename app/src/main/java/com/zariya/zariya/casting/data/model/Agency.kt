package com.zariya.zariya.casting.data.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable {
    @IgnoredOnParcel
    @get:Exclude
    var agencyId: String? = ""
}