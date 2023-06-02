package com.zariya.zariya.casting.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import com.zariya.zariya.R
import com.zariya.zariya.casting.presentation.adapter.CastingFeedCalendarAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentVolunteerCastingFeedsBinding
import com.zariya.zariya.databinding.LayoutCalendarDayBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class VolunteerCastingFeedsFragment : BaseFragment() {

    private lateinit var binding: FragmentVolunteerCastingFeedsBinding

    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDate? = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVolunteerCastingFeedsBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWeekCalendar()
        binding.rvFeeds.apply {
            adapter = CastingFeedCalendarAdapter(onItemClick = {
                Navigation.findNavController(binding.root)
                    .navigate(VolunteerCastingFeedsFragmentDirections.actionSwipeActors())
            }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWeekCalendar() {
        class WeekDayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: WeekDay
            val binding = LayoutCalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    dateClicked(date = day.date)
                }
            }

            fun bind(day: WeekDay) {
                this.day = day
                binding.tvDayOfMonth.text = dateFormatter.format(day.date)
                binding.tvDayOfWeek.text = getDayOfWeek(day.date.dayOfWeek.toString())

                context?.let { ctx ->
                    binding.tvDayOfMonth.setTextColor(
                        ContextCompat.getColor(
                            ctx,
                            if (day.date == selectedDate) {
                                R.color.blue_400
                            } else {
                                R.color.white
                            }
                        )
                    )
                }
                binding.viewSelected.isVisible = day.date == selectedDate
            }
        }

        binding.calendar.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)
            override fun bind(container: WeekDayViewContainer, data: WeekDay) = container.bind(data)
        }
        binding.calendar.weekScrollListener = {
            binding.toolBarTitle.text = getWeekPageTitle(it)
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        binding.calendar.setup(
            startMonth.atStartOfMonth(),
            endMonth.atEndOfMonth(),
            daysOfWeek().first(),
        )
        binding.calendar.scrollToDate(LocalDate.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateClicked(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            binding.calendar.notifyDateChanged(date)
            oldDate?.let { binding.calendar.notifyDateChanged(it) }
        }
    }

    fun getDayOfWeek(day: String) = when (day) {
        "SUNDAY" -> "Sun"
        "MONDAY" -> "Mon"
        "TUESDAY" -> "Tue"
        "WEDNESDAY" -> "Wed"
        "THURSDAY" -> "Thu"
        "FRIDAY" -> "Fri"
        else -> "Sat"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekPageTitle(week: Week): String {
        val firstDate = week.days.first().date
        val lastDate = week.days.last().date
        return when {
            firstDate.yearMonth == lastDate.yearMonth -> {
                firstDate.yearMonth.month.name
            }

            firstDate.year == lastDate.year -> {
                "${firstDate.month.name} - ${lastDate.yearMonth.month.name}"
            }

            else -> {
                "${firstDate.yearMonth.month.name} - ${lastDate.yearMonth.month.name}"
            }
        }
    }
}