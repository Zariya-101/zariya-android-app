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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.zariya.zariya.casting.presentation.adapter.AddImageAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentAddImagesBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddImagesFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentAddImagesBinding

    private val PERMISSION_READ_MEDIA_IMAGES = 1
    private val PERMISSION_WRITE_EXTERNAL = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImagesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiEventListener()

        binding.rvImages.apply {
            adapter = AddImageAdapter(castingOnboardingViewModel.actorProfileDetails.imageList,
                onItemClick = {
                    checkGalleryPermission()
                },
                onCloseClick = {
                    castingOnboardingViewModel.actorProfileDetails.imageList.remove(it)
                    binding.rvImages.adapter?.notifyDataSetChanged()
                })
        }
    }

    private fun checkGalleryPermission() {
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

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    castingOnboardingViewModel.uploadImage(it, onUploaded = {
                        binding.rvImages.adapter?.notifyDataSetChanged()
                    })
                }
            }
        }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResultLauncher.launch(galleryIntent)
    }

    private fun uiEventListener() {
        castingOnboardingViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    // Handle Loading
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context,
                        uiEvent.message ?: "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {}

                is UIEvents.RefreshUi -> {
                    binding.rvImages.adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}