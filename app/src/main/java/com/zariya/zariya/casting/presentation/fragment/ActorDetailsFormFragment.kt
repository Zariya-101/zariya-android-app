package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentActorDetailsFormBinding

class ActorDetailsFormFragment : BaseFragment() {

    private lateinit var binding: FragmentActorDetailsFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActorDetailsFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        initHeightSpinner()
        initComplexionSpinner()
    }

    private fun setUpListeners() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun initHeightSpinner() {
        context?.let {
            val heights = resources.getStringArray(R.array.actor_height)
            val arrayAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, heights)
            arrayAdapter.setDropDownViewResource(R.layout.item_spinner)

            with(binding.spinnerHeight) {
                adapter = arrayAdapter
                setSelection(0, true)
                prompt = "Select your Height"
                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Toast.makeText(it, heights[position], Toast.LENGTH_LONG).show()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            }
        }
    }

    private fun initComplexionSpinner() {
        context?.let {
            val complexions = resources.getStringArray(R.array.actor_complexion)
            val arrayAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, complexions)
            arrayAdapter.setDropDownViewResource(R.layout.item_spinner)

            with(binding.spinnerComplexion) {
                adapter = arrayAdapter
                setSelection(0, true)
                prompt = "Select your Complexion"
                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Toast.makeText(it, complexions[position], Toast.LENGTH_LONG).show()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            }
        }
    }
}