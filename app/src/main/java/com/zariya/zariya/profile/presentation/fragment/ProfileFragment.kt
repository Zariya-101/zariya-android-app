package com.zariya.zariya.profile.presentation.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zariya.zariya.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    private val PERMISSION_READ_MEDIA_IMAGES = 1
    private val PERMISSION_WRITE_EXTERNAL = 1

    var profileImageUrl: Uri? = null
    var imageUri: Uri? = null

    private val IMAGE_GALLERY_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivUserImage.setOnClickListener{
            checkGalleryPermission()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
           IMAGE_GALLERY_REQUEST
        )
    }

    private fun checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf<String>(Manifest.permission.READ_MEDIA_IMAGES),
                        PERMISSION_READ_MEDIA_IMAGES
                    )
                }
            } else {
                openGallery()
            }
        } else {
            if (context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                } != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_WRITE_EXTERNAL
                    )
                }
            } else {
                openGallery()
            }
        }
    }

}