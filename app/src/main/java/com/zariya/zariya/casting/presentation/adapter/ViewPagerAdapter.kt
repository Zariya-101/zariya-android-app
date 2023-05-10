package com.zariya.zariya.casting.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectAgeFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectHeightFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectUserTypeFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
//        bundle.putParcelable("course", course)
        val fragment = when (position) {
//            1 -> CourseScheduleFragment()
//            2 -> CourseSyllabusFragment()
//            3 -> CourseReviewFragment()
//            else -> AboutCourseFragment()
            1 -> SelectAgeFragment()
            2 -> SelectHeightFragment()
            else -> SelectUserTypeFragment()
        }
//        fragment.arguments = bundle
        return fragment
    }
}