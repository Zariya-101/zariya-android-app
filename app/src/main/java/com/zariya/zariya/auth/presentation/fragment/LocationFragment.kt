package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.auth.presentation.adapter.OtherCitiesAdapter
import com.zariya.zariya.auth.presentation.adapter.PopularCitiesAdapter
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentLocationBinding
import com.zariya.zariya.utils.getCities
import com.zariya.zariya.utils.toCitiesList
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class LocationFragment : BaseFragment() {

    private lateinit var binding: FragmentLocationBinding
    private val authViewModel by viewModels<AuthViewModel>()

    private val cityImages =
        intArrayOf(
            R.drawable.mumbai, R.drawable.banglore, R.drawable.banglore,
            R.drawable.hyderabad, R.drawable.ahemdabad,
            R.drawable.chennai, R.drawable.chennai
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        binding.rvPopularCities.apply {
            adapter = PopularCitiesAdapter(
                resources.getStringArray(R.array.popular_cities_array), cityImages
            ) {
                authViewModel.updateCurrentCity(it)
            }
        }
        binding.rvOtherCities.apply {
            context.getCities()?.let { cities ->
                val jsonObject = JSONObject(cities)
                val citiesList = jsonObject.getJSONArray("array").toCitiesList()
                adapter = OtherCitiesAdapter(citiesList, onItemClick = {
                    authViewModel.updateCurrentCity(it)
                })
            }
        }
    }

    private fun setUpListeners() {
        uiEventListener()
        authViewModel.currentCityLiveData.observe(viewLifecycleOwner) {
            binding.currentCity = it
            it?.let { city ->
                authViewModel.updateUserLocation(city)
            } ?: run {
                Toast.makeText(context, "Please Select City", Toast.LENGTH_LONG).show()
            }
        }
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        binding.tvAutoDetect.setOnClickListener {

        }
    }

    private fun uiEventListener() {
        authViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
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