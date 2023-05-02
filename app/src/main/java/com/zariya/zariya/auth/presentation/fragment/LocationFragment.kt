package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zariya.zariya.R
import com.zariya.zariya.adapter.OtherCitiesAdapter
import com.zariya.zariya.adapter.PopularCitiesAdapter
import com.zariya.zariya.databinding.FragmentLocationBinding


class LocationFragment : Fragment() {

    private lateinit var binding: FragmentLocationBinding
    private val cityImages =
        intArrayOf(
            R.drawable.ahemdabad, R.drawable.banglore, R.drawable.mumbai,
            R.drawable.chennai, R.drawable.hyderabad,
            R.drawable.ahemdabad, R.drawable.banglore, R.drawable.mumbai,
            R.drawable.chennai, R.drawable.hyderabad,
        )

    private val cityNames =
        arrayOf<String>("Ahmedabad","Banglore","Mumbai","Chennai","Hyderabad","Ahmedabad","Banglore","Mumbai","Chennai","Hyderabad")
    private lateinit var popularCitiesAdapter: PopularCitiesAdapter

    private val otherCityNames =
        arrayOf<String>("Ahmedabad","Banglore")

    private lateinit var otherCitiesAdapter: OtherCitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        showCitiesList()
        setSearchFunctionality()

    }

    private fun setSearchFunctionality() {

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {


            }
        })
    }

    private fun showCitiesList() {

        popularCitiesAdapter =  PopularCitiesAdapter(cityNames,cityImages)
        binding.rvPopularCities.adapter = popularCitiesAdapter

        otherCitiesAdapter = OtherCitiesAdapter(otherCityNames)
        binding.rvOtherCities.adapter = otherCitiesAdapter

    }

}