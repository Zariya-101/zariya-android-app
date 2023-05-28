package com.zariya.zariya.core.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    private val PERMISSION_READ_MEDIA_IMAGES = 1
    private val PERMISSION_WRITE_EXTERNAL = 1

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
    }

    fun showProgress(view: View) {
        ProgressView.showProgress(view, activity)
    }

    fun hideProgress() {
        ProgressView.hideProgress()
    }

    fun checkGalleryPermission(openGallery: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context?.let {
                    ContextCompat.checkSelfPermission(it, Manifest.permission.READ_MEDIA_IMAGES)
                } != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        PERMISSION_READ_MEDIA_IMAGES
                    )
                }
            } else {
                openGallery()
            }
        } else {
            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_WRITE_EXTERNAL
                    )
                }
            } else {
                openGallery()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}