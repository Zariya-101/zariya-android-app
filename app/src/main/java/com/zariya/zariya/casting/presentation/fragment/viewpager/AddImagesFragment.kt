package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zariya.zariya.casting.presentation.adapter.AddImageAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentAddImagesBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddImagesFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentAddImagesBinding

    private val PERMISSION_READ_MEDIA_IMAGES = 1
    private val PERMISSION_WRITE_EXTERNAL = 1

    private var imagesList: ArrayList<String?> = arrayListOf("")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImagesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvImages.apply {
            adapter = AddImageAdapter(imagesList,
                onItemClick = {
                    checkGalleryPermission()
                },
                onCloseClick = {
                    imagesList.remove(it)
                    binding.rvImages.adapter?.notifyDataSetChanged()
                })
        }
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

    var galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            imagesList.add(0, uri.toString())
            binding.rvImages.adapter?.notifyDataSetChanged()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(galleryIntent)
    }
}