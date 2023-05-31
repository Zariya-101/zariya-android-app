package com.zariya.zariya.casting.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.CastingCall
import com.zariya.zariya.casting.presentation.viewmodel.CreateCastingCallViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.ProgressView
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentCreateCastingCallBinding
import com.zariya.zariya.utils.PAID
import com.zariya.zariya.utils.UNPAID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCastingCallFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateCastingCallBinding
    private val viewModel by viewModels<CreateCastingCallViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCastingCallBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
        getVolunteersList()
    }

    private fun initView() {
        val genderArray = resources.getStringArray(R.array.gender_array)
        binding.tvGender.setAdapter(context?.let {
            ArrayAdapter(it, R.layout.item_dropdown, genderArray)
        })

        val ageArray = resources.getStringArray(R.array.age_range_array)
        binding.tvAge.setAdapter(context?.let {
            ArrayAdapter(it, R.layout.item_dropdown, ageArray)
        })

        val complexionArray = resources.getStringArray(R.array.actor_complexion)
        binding.tvComplexion.setAdapter(context?.let {
            ArrayAdapter(it, R.layout.item_dropdown, complexionArray)
        })
    }

    private fun setUpListeners() {
        uiEventListener()

        binding.rbPaid.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.tilPaid.editText?.setText("")
                binding.tilPaid.editText?.isEnabled = true
            } else {
                binding.tilPaid.editText?.setText(getString(R.string.unpaid))
                binding.tilPaid.editText?.isEnabled = false
            }
        }

        binding.layoutUploadImage.root.setOnClickListener {
            checkGalleryPermission {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResultLauncher.launch(galleryIntent)
            }
        }

        binding.layoutUploadImage.ivClose.setOnClickListener {
            viewModel.imageUri = null
            binding.layoutUploadImage.image = null
        }

        binding.btnCreate.setOnClickListener {
            if (validate()) {
                create()
            }
        }
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.imageUri = result.data?.data
                binding.layoutUploadImage.image = viewModel.imageUri.toString()
            }
        }

    private fun create() {
        viewModel.selectedVolunteer?.let { volunteerId ->
            viewModel.agencyId?.let { agencyId ->
                showProgress(binding.root)
                val castingCall = CastingCall(
                    agencyId = agencyId,
                    gender = binding.tvGender.text.toString(),
                    role = binding.tilRole.editText?.text.toString(),
                    ageRange = binding.tvAge.text.toString(),
                    complexion = binding.tvComplexion.text.toString(),
                    paymentType = if (binding.rbPaid.isChecked) PAID else UNPAID,
                    payment = if (binding.rbPaid.isChecked) binding.tilPaid.editText?.text.toString() else null,
                    description = binding.tilDescription.editText?.text.toString(),
                    assignedTo = volunteerId
                )
                viewModel.createCastingCall(castingCall)
            }
        }
    }

    private fun getVolunteersList() {
        viewModel.getVolunteersForMyAgency()
        viewModel.volunteersLiveData.observe(viewLifecycleOwner) { volunteers ->
            binding.tvAssignTo.setAdapter(context?.let {
                ArrayAdapter(it, R.layout.item_dropdown, volunteers.map { it?.name })
            })
            binding.tvAssignTo.setOnItemClickListener { _, _, position, _ ->
                viewModel.selectedVolunteer = volunteers[position]?.volunteerId
            }
        }
    }

    private fun validate(): Boolean {

        if (binding.tvGender.text.toString().isEmpty() ||
            binding.tvGender.text.toString() == getString(R.string.looking_for)
        ) {
            binding.tilGender.error = getString(R.string.validation_empty_gender)
            binding.nsv.scrollTo(0, binding.tilGender.top)
            return false
        }
        binding.tilGender.isErrorEnabled = false

        if (binding.tilRole.editText?.text?.isEmpty() == true) {
            binding.tilRole.error = getString(R.string.validation_empty_role)
            binding.nsv.scrollTo(0, binding.tilRole.top)
            return false
        }
        binding.tilRole.isErrorEnabled = false

        if (binding.tvAge.text.toString().isEmpty() ||
            binding.tvAge.text.toString() == getString(R.string.age)
        ) {
            binding.tilAge.error = getString(R.string.validation_empty_age)
            binding.nsv.scrollTo(0, binding.tilAge.top)
            return false
        }
        binding.tilAge.isErrorEnabled = false

        if (binding.tvComplexion.text.toString().isEmpty() ||
            binding.tvComplexion.text.toString() == getString(R.string.complexion)
        ) {
            binding.tilComplexion.error = getString(R.string.validation_empty_complexion)
            binding.nsv.scrollTo(0, binding.tilComplexion.top)
            return false
        }
        binding.tilComplexion.isErrorEnabled = false

        if (binding.tilPaid.editText?.text?.isEmpty() == true) {
            binding.tilPaid.error = getString(R.string.validation_empty_payment)
            binding.nsv.scrollTo(0, binding.tilPaid.top)
            return false
        }
        binding.tilPaid.isErrorEnabled = false

        if (binding.tilDescription.editText?.text?.isEmpty() == true) {
            binding.tilDescription.error = getString(R.string.validation_empty_description)
            binding.nsv.scrollTo(0, binding.tilDescription.top)
            return false
        }
        binding.tilDescription.isErrorEnabled = false

        if (viewModel.selectedVolunteer.isNullOrEmpty()) {
            binding.tilAssignTo.error = getString(R.string.validation_empty_volunteer)
            binding.nsv.scrollTo(0, binding.tilAssignTo.top)
            return false
        }
        binding.tilAssignTo.isErrorEnabled = false

        return true
    }

    private fun uiEventListener() {
        viewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
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
                    } ?: run {
                        Navigation.findNavController(binding.root).popBackStack()
                    }
                }

                is UIEvents.RefreshUi -> {}
            }
        }
    }
}