package com.zariya.zariya.casting.data.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SelectUserTypeModel(
    val title: String,
    val subTitle: String,
    val icon: Drawable?,
    val isSelected: Boolean = false
)

@Parcelize
data class ActorProfile(
    var name: String = "",
    var userId: String = "",
    val age: String = "",
    val complexion: String = "",
    val height: String = "",
    val imageList: ArrayList<String> = arrayListOf("")
): Parcelable