package com.zariya.zariya.core.ui

import android.app.Activity
import android.view.View
import com.shasin.notificationbanner.Banner
import com.zariya.zariya.R

object ProgressView {
    private var banner: Banner? = null
    private fun show(view: View, activity: Activity?) {
        if (activity != null) {
            banner = Banner(view, activity)
            banner!!.isAsDropDown = true
            banner!!.isFillScreen = true
            banner!!.setLayout(R.layout.progress_view)
            banner!!.show()
        }
    }

    private fun hide() {
        if (banner != null) {
            banner!!.dismissBanner()
        }
    }

    fun showProgress(view: View, activity: Activity?) {
        show(view, activity)
    }

    fun hideProgress() {
        hide()
    }
}