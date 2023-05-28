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
import com.zariya.zariya.casting.presentation.adapter.AddImageAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentAddImagesBinding
import com.zariya.zariya.utils.ACTOR_PROFILE_IMAGE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddImagesFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentAddImagesBinding

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
            adapter = AddImageAdapter(castingOnboardingViewModel.actorProfileDetails.imageList,
                onItemClick = {
                    checkGalleryPermission {
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryActivityResultLauncher.launch(galleryIntent)
                    }
                },
                onCloseClick = {
                    castingOnboardingViewModel.actorProfileDetails.imageList.remove(it)
                    binding.rvImages.adapter?.notifyDataSetChanged()
                })
        }
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    castingOnboardingViewModel.uploadImage(it, ACTOR_PROFILE_IMAGE, onUploaded = {
                        binding.rvImages.adapter?.notifyDataSetChanged()
                    })
                }
            }
        }
}