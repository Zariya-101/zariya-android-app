package com.zariya.zariya.casting.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.presentation.viewmodel.AgencyDetailsFormViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentAgencyDetailsFormBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import java.util.Calendar

@AndroidEntryPoint
class AgencyDetailsFormFragment : BaseFragment() {

    private lateinit var binding: FragmentAgencyDetailsFormBinding
    private val viewModel by viewModels<AgencyDetailsFormViewModel>()

    private val specialityListItems = arrayOf("Tv Series", "Web Series", "Ads", "Movies")
    private val specialityCheckedItems = BooleanArray(specialityListItems.size)
    private val specialitySelectedItems = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgencyDetailsFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        populatePreFilledData()
    }

    private fun populatePreFilledData() {
        viewModel.getUserDetails()?.let { user ->
            if (user.name.isNullOrEmpty().not()) {
                binding.tilName.editText?.setText(user.name)
            }
            if (user.phone.isNullOrEmpty().not()) {
                binding.tilPhone.editText?.setText(user.phone)
                binding.tilPhone.editText?.isEnabled = false
            }
            if (user.email.isNullOrEmpty().not()) {
                binding.tilEmail.editText?.setText(user.email)
                binding.tilEmail.editText?.isEnabled = false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        uiEventsListener()

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.tilStarted.editText?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val cal = Calendar.getInstance()
                activity?.let {
                    val datePickerDialog = DatePickerDialog(
                        it, { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                            val selectedDate =
                                dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                            binding.tilStarted.editText?.setText(selectedDate)
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                }
            }

            return@setOnTouchListener false
        }

        binding.tilSpeciality.editText?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                selectSpeciality()
            }

            return@setOnTouchListener false
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

        binding.btnRegister.setOnClickListener {
            if (validate()) {
                register()
            }
        }
    }

    private fun selectSpeciality() {
        AlertDialog.Builder(context).apply {
            setTitle("Choose Speciality")
            setMultiChoiceItems(
                specialityListItems, specialityCheckedItems
            ) { _, which, isChecked ->
                specialityCheckedItems[which] = isChecked
            }
            setCancelable(false)
            setPositiveButton("Done") { _, _ ->
                specialitySelectedItems.clear()
                for (i in specialityCheckedItems.indices) {
                    if (specialityCheckedItems[i]) {
                        specialitySelectedItems.add(specialityListItems[i])
                        binding.tilSpeciality.editText?.setText(
                            String.format(
                                "%s%s, ", binding.tilSpeciality.editText?.text.toString(),
                                specialityListItems[i]
                            )
                        )
                    }
                }
            }
            setNegativeButton("Cancel") { _, _ -> }
            setNeutralButton("Clear All") { _, _ ->
                specialitySelectedItems.clear()
                Arrays.fill(specialityCheckedItems, false)
                binding.tilSpeciality.editText?.setText("")
            }
            create().show()
        }
    }

    private fun register() {
        showProgress(binding.root)
        val agency = Agency(
            name = binding.tilName.editText?.text.toString(),
            startedIn = binding.tilStarted.editText?.text.toString(),
            email = binding.tilEmail.editText?.text.toString(),
            phone = binding.tilPhone.editText?.text.toString(),
            description = binding.tilDescription.editText?.text.toString(),
            speciality = specialitySelectedItems,
            knownFor = binding.tilKnownFor.editText?.text.toString()
        )
        viewModel.registerAgency(agency)
    }

    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.imageUri = result.data?.data
                binding.layoutUploadImage.image = viewModel.imageUri.toString()
            }
        }

    private fun validate(): Boolean {
        if (viewModel.imageUri == null) {
            Toast.makeText(context, "Please Select Profile Picture", Toast.LENGTH_LONG).show()
            return false
        }

        if (binding.tilName.editText?.text?.isEmpty() == true) {
            binding.tilName.error = getString(R.string.validation_empty_name)
            return false
        }
        binding.tilName.isErrorEnabled = false

        if (binding.tilStarted.editText?.text?.isEmpty() == true) {
            binding.tilStarted.error = getString(R.string.validation_empty_started_in)
            return false
        }
        binding.tilStarted.isErrorEnabled = false

        if (binding.tilEmail.editText?.text?.isEmpty() == true) {
            binding.tilEmail.error = getString(R.string.validation_empty_email)
            return false
        }
        binding.tilEmail.isErrorEnabled = false

        if (binding.tilPhone.editText?.text?.isEmpty() == true) {
            binding.tilPhone.error = getString(R.string.validation_empty_phone)
            return false
        }
        binding.tilPhone.isErrorEnabled = false

        if (binding.tilPhone.editText?.text?.toString()?.length != 10) {
            binding.tilPhone.error = getString(R.string.validation_invalid_phone)
            return false
        }
        binding.tilPhone.isErrorEnabled = false

        if (binding.tilDescription.editText?.text?.isEmpty() == true) {
            binding.tilDescription.error = getString(R.string.validation_empty_description)
            return false
        }
        binding.tilDescription.isErrorEnabled = false

        if (binding.tilSpeciality.editText?.text?.isEmpty() == true) {
            binding.tilSpeciality.error = getString(R.string.validation_empty_speciality)
            return false
        }
        binding.tilSpeciality.isErrorEnabled = false

        if (binding.tilKnownFor.editText?.text?.isEmpty() == true) {
            binding.tilKnownFor.error = getString(R.string.validation_empty_known_For)
            return false
        }
        binding.tilKnownFor.isErrorEnabled = false

        return true
    }

    private fun uiEventsListener() {
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
                    }
                }

                is UIEvents.RefreshUi -> {}
            }
        }
    }
}