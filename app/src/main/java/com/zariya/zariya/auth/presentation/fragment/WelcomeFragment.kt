package com.zariya.zariya.auth.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.zariya.zariya.R
import com.zariya.zariya.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {
    private lateinit var layouts: IntArray
    private lateinit var dots: Array<TextView?>
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layouts = intArrayOf(
            R.layout.welcome_slide1,
            R.layout.welcome_slide2,
            R.layout.welcome_slide3,
            R.layout.welcome_slide4
        )

        // adding bottom dots
        addBottomDots(0)

        //setup viewpager
        myViewPagerAdapter = MyViewPagerAdapter()
        binding.viewPager.adapter = myViewPagerAdapter
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        //click events
        binding.btnNext.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                binding.viewPager.currentItem = current
            } else {
            }
        }
        binding.btnSkip.setOnClickListener {
           binding.viewPager.currentItem = 3
        }
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    private fun getItem(i: Int): Int {
        return binding.viewPager.currentItem + i
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(context)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            binding.layoutDots.addView(dots[i])
        }
        if (dots.size > 0) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (position == 0) {
                binding.btnSkip.setTextColor(resources.getColor(R.color.dark_blue))
                binding.btnNext.setTextColor(resources.getColor(R.color.white))
            } else {
                binding.btnNext.setTextColor(Color.WHITE)
            }
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                binding.btnNext.setTextColor(resources.getColor(R.color.white))
                binding.btnNext.setText(resources.getString(R.string.start))
                binding.btnSkip.visibility = View.GONE
                binding.btnNext.visibility=View.GONE
            } else {
                // still pages are left
                binding.btnNext.text = getString(R.string.next)
                binding.btnSkip.visibility = View.VISIBLE
                binding.btnNext.visibility=View.VISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}