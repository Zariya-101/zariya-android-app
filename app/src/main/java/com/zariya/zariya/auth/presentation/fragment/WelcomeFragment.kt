package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.zariya.zariya.R
import com.zariya.zariya.auth.data.model.IntroSlide
import com.zariya.zariya.auth.presentation.adapter.IntroSlideAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentWelcomeBinding

class WelcomeFragment : BaseFragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val introSlideAdapter = IntroSlideAdapter(
        listOf(
            IntroSlide(description = R.string.intro_acting, drawable = R.drawable.anim_acting),
            IntroSlide(description = R.string.intro_casting, drawable = R.drawable.anim_casting),
            IntroSlide(
                description = R.string.intro_meditation,
                drawable = R.drawable.anim_meditation
            ),
            IntroSlide(
                description = R.string.intro_merchandise,
                drawable = R.drawable.anim_merchandise
            ),
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        binding.viewPager.adapter = introSlideAdapter
        setupIndicator()
        setCurrentIndicator(0)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
    }

    private fun setUpListeners() {
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem + 1 < introSlideAdapter.itemCount) {
                binding.viewPager.currentItem += 1
            } else {
                Navigation.findNavController(binding.root)
                    .navigate(WelcomeFragmentDirections.actionWelcomeToLogin())
            }
        }

        binding.btnSkip.setOnClickListener {
            binding.viewPager.currentItem = introSlideAdapter.itemCount - 1
        }
    }

    private fun setupIndicator() {
        val indicators = arrayOfNulls<ImageView>(introSlideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(context?.applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context.applicationContext, R.drawable.intro_indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.llIndicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        if (index + 1 == introSlideAdapter.itemCount) {
            binding.btnSkip.visibility = View.GONE
            binding.btnNext.text = getString(R.string.login)
        } else {
            binding.btnSkip.visibility = View.VISIBLE
            binding.btnNext.text = getString(R.string.next)
        }
        val childCount = binding.llIndicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.llIndicatorContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext().applicationContext, R.drawable.intro_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext().applicationContext, R.drawable.intro_indicator_inactive
                    )
                )
            }
        }
    }
}