package com.zariya.zariya.casting.data.model

import android.graphics.drawable.Drawable

data class SelectUserTypeModel(
    val title: String,
    val subTitle: String,
    val icon: Drawable,
    val isSelected: Boolean = false
)