package com.zariya.zariya.casting.data.model

import android.graphics.drawable.Drawable

data class SelectUserTypeModel(
    val title: String,
    val subTitle: String,
    val icon: Drawable,
    val isSelected: Boolean = false
)

data class ActorProfile(
    var userId: String = "",
    val age: String = "",
    val complexion: String = "",
    val height: String = ""
)