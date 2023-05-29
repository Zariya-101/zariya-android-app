package com.zariya.zariya.casting.presentation.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.Volunteer
import com.zariya.zariya.casting.presentation.viewmodel.VolunteerOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentVolunteerOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays

@AndroidEntryPoint
class VolunteerOnboardingFragment : BaseFragment() {

    private lateinit var binding: FragmentVolunteerOnboardingBinding
    private val viewModel by viewModels<VolunteerOnboardingViewModel>()

    private val specialityListItems = arrayOf("Tv Series", "Web Series", "Ads", "Movies")
    private val specialityCheckedItems = BooleanArray(specialityListItems.size)
    private val specialitySelectedItems = arrayListOf<String>()

    val experienceSelectedItems = intArrayOf(-1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVolunteerOnboardingBinding.inflate(layoutInflater)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        uiEventsListener()

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.tilExperience.editText?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                selectExperience()
            }

            return@setOnTouchListener false
        }

        binding.tilSpeciality.editText?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                selectSpeciality()
            }

            return@setOnTouchListener false
        }



        binding.btnRegister.setOnClickListener {
            if (validate()) {
                register()
            }
        }
    }

    private fun selectExperience() {
        AlertDialog.Builder(context).apply {
            setTitle("Choose your Experience")
            val listItems = arrayOf("Fresher", "0-2 years", "2-5 years", "5-10 years", "10+ years")
            setSingleChoiceItems(listItems, experienceSelectedItems[0]) { dialog, which ->
                experienceSelectedItems[0] = which
                binding.tilExperience.editText?.setText(listItems[which])
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { _, _ -> }

            create().show()
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

    private fun register() {
        showProgress(binding.root)
        val volunteer = Volunteer(
            name = binding.tilName.editText?.text.toString(),
            email = binding.tilEmail.editText?.text.toString(),
            phone = binding.tilPhone.editText?.text.toString(),
            speciality = specialitySelectedItems,
            experience = binding.tilExperience.editText?.text.toString(),
            worksFor = binding.tilWorksFor.editText?.text.toString()
        )
        viewModel.registerVolunteer(volunteer)
    }

    private fun validate(): Boolean {

        if (binding.tilName.editText?.text?.isEmpty() == true) {
            binding.tilName.error = getString(R.string.validation_empty_name)
            return false
        }
        binding.tilName.isErrorEnabled = false

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

        if (binding.tilExperience.editText?.text?.isEmpty() == true) {
            binding.tilExperience.error = getString(R.string.validation_empty_experience)
            return false
        }
        binding.tilExperience.isErrorEnabled = false

        if (binding.tilSpeciality.editText?.text?.isEmpty() == true) {
            binding.tilSpeciality.error = getString(R.string.validation_empty_speciality)
            return false
        }
        binding.tilSpeciality.isErrorEnabled = false

        if (binding.tilWorksFor.editText?.text?.isEmpty() == true) {
            binding.tilWorksFor.error = getString(R.string.validation_empty_works_for)
            return false
        }
        binding.tilWorksFor.isErrorEnabled = false

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