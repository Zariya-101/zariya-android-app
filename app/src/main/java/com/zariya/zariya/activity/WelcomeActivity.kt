package com.zariya.zariya.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var layouts: IntArray
    private lateinit var dots: Array<TextView?>
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var binding: ActivityWelcomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        binding = ActivityWelcomeBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        initialization()
    }

    private fun initialization() {

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
        binding!!.viewPager.adapter = myViewPagerAdapter
        binding!!.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        //click events
        binding!!.btnNext.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                binding!!.viewPager.currentItem = current
            } else {
            }
        }
//        binding!!.btnSkip.setOnClickListener { }
    }

    fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        return binding!!.viewPager.currentItem + i
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        binding!!.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            binding!!.layoutDots.addView(dots[i])
        }
        if (dots.size > 0) dots[currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
            if (position == 0) {
//                binding!!.btnSkip.setTextColor(resources.getColor(R.color.white))
                binding!!.btnNext.setTextColor(resources.getColor(R.color.white))
            } else {
//                binding!!.btnSkip.setTextColor(Color.WHITE)
                binding!!.btnNext.setTextColor(Color.WHITE)
            }
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                binding!!.btnNext.setTextColor(resources.getColor(R.color.white))
                binding!!.btnNext.text = "Start"
//                binding!!.btnSkip.visibility = View.GONE
            } else {
                // still pages are left
                binding!!.btnNext.text = getString(R.string.next)
//                binding!!.btnSkip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}