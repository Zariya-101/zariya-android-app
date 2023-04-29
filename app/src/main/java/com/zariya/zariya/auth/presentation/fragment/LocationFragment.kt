package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        intArrayOf(R.string.ahmedabad,R.string.banglore,R.string.mumbai,R.string.chennai,R.string.hyderbad,R.string.ahmedabad,R.string.banglore,R.string.mumbai,R.string.chennai,R.string.hyderbad)
    private lateinit var popularCitiesAdapter: PopularCitiesAdapter

    private val otherCityNames =
        intArrayOf(R.string.hyderbad,R.string.banglore,R.string.ahmedabad,R.string.banglore,R.string.mumbai,R.string.chennai,R.string.hyderbad)

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
                val search = s.toString()

            }
        });
    }

    private fun showCitiesList() {

        val layoutManager = GridLayoutManager(activity, 4)
        binding.rvPopularCities.setLayoutManager(layoutManager)
        popularCitiesAdapter =  PopularCitiesAdapter(cityNames,cityImages)
        binding.rvPopularCities.setAdapter(popularCitiesAdapter)


        binding.rvOtherCities.setLayoutManager(LinearLayoutManager(context))
        otherCitiesAdapter = OtherCitiesAdapter(otherCityNames)
        binding.rvOtherCities.setAdapter(otherCitiesAdapter)

    }

}