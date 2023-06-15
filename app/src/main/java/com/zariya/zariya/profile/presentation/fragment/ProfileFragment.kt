package com.zariya.zariya.profile.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentProfileBinding
import com.zariya.zariya.profile.presentation.ProfileViewModel
import com.zariya.zariya.utils.AppUtil
import com.zariya.zariya.utils.USER_COVER_PIC
import com.zariya.zariya.utils.USER_PROFILE_PIC
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        profileViewModel.user.observe(viewLifecycleOwner) {
            binding.user = it
            setUserAgeAndGender(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUserAgeAndGender(user: User) {
        var userDetails = ""
        if (user.gender.isNullOrEmpty().not()) {
            userDetails = user.gender!!
//            userDetails = if (user.gender.equals(getString(R.string.male))) {
//                "M"
//            } else if (user.gender.equals(getString(R.string.female))) {
//                "F"
//            } else if (user.gender.equals(getString(R.string.others))) {
//                "O"
//            } else ""
        }
        if (user.dob.isNullOrEmpty().not()) {
            val dob = user.dob!!.split("/")
            val dayOfMonth = dob[0]
            val month = dob[1]
            val year = dob[2]

            val age = AppUtil.getAge(year.toInt(), month.toInt(), dayOfMonth.toInt())
            userDetails += if (userDetails == "") "$age" else ", $age"
        }
        binding.tvUserDetails.text = userDetails
    }

    private fun setUpListeners() {
        uiEventListener()
        binding.cvProfilePic.setOnClickListener {
            checkGalleryPermission {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                profilePicGalleryLauncher.launch(galleryIntent)
            }
        }

        binding.cvEditCoverPic.setOnClickListener {
            checkGalleryPermission {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                coverPicGalleryLauncher.launch(galleryIntent)
            }
        }
    }

    private var profilePicGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    showProgress(binding.root)
                    profileViewModel.uploadImage(it, USER_PROFILE_PIC, onUploaded = { uploadedUri ->
                        profileViewModel.user.value?.let { user ->
                            profileViewModel.updateUser(user.copy(profilePic = uploadedUri.toString()))
                        }
                    })
                }
            }
        }

    private var coverPicGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    showProgress(binding.root)
                    profileViewModel.uploadImage(it, USER_COVER_PIC, onUploaded = { uploadedUri ->
                        profileViewModel.user.value?.let { user ->
                            profileViewModel.updateUser(user.copy(coverPic = uploadedUri.toString()))
                        }
                    })
                }
            }
        }

    private fun uiEventListener() {
        profileViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    if (uiEvent.loading) showProgress(binding.root) else hideProgress()
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context, uiEvent.message ?: "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.ShowSuccess -> {
                    Toast.makeText(
                        context, uiEvent.message ?: "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {
                    uiEvent.navDirections?.let {
                        Navigation.findNavController(binding.root).navigate(it)
                    }
                }

                is UIEvents.RefreshUi -> {}
            }
        }
    }

}