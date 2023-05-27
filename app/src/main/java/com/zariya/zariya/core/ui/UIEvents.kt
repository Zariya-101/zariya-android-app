package com.zariya.zariya.core.ui

import androidx.navigation.NavDirections

sealed class UIEvents {
    data class ShowError(val message: String?) : UIEvents()

    data class Loading(val loading: Boolean) : UIEvents()

    data class Navigate(val navDirections: NavDirections?) : UIEvents()

    data class RefreshUi(val message: String) : UIEvents()
}