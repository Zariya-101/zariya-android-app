package com.zariya.zariya.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible

object AppUtil {

    fun hideKeyboard(editText: EditText, context: Context) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun setVisibility(show: Boolean, vararg views: View) {
        for (view in views) view.isVisible = show
    }

}