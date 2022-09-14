package com.q2ve.schedappv2.ui.core.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.databinding.ScheduleBinding
import com.q2ve.schedappv2.helpers.ButtonAnimator

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleFragment: Fragment() {
	private lateinit var viewModel: ScheduleViewModel
	private lateinit var viewPager: ViewPager2
	private lateinit var weekParityButton: TextView
	private lateinit var errorTextView: TextView
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = ScheduleBinding.inflate(inflater, container, false)
		
		viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
		viewModel.onCreateView(
			inflater,
			binding.scheduleButtonBarFrame,
			resources,
			requireActivity().theme
		)
		
		//ViewPager
		viewPager = binding.coreScheduleModulePager
		val pageTransformer = ViewPager2.PageTransformer { page, position ->
			page.apply {
				val r = 1 - kotlin.math.abs(position)
				page.alpha = 0.25f + r
				page.scaleY = 0.75f + r * 0.25f

			}
		}
		viewPager.setPageTransformer(pageTransformer)
		viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageScrolled(
				position: Int,
				positionOffset: Float,
				positionOffsetPixels: Int
			) {
				//TODO("Something cool!")
				super.onPageScrolled(position, positionOffset, positionOffsetPixels)
			}

			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				viewModel.onPagerPageSelected(position)
			}

			override fun onPageScrollStateChanged(state: Int) {
				//TODO("Something cool!")
				super.onPageScrollStateChanged(state)
			}
		})
		placeWaitingSpinner()

		//Setting up buttons and views
		weekParityButton = binding.coreScheduleWeekParity
		weekParityButton.setOnClickListener { viewModel.onParityButtonPressed() }
		ButtonAnimator(weekParityButton).animateStrongPressingWithFading()
		
		val filterButton = binding.coreScheduleGroupFilter
		filterButton.setOnClickListener { viewModel.onFilterButtonPressed() }
		ButtonAnimator(filterButton).animateStrongPressing()
		
		val settingsButton = binding.coreScheduleUserSettings
		settingsButton.setOnClickListener { viewModel.onSettingsButtonPressed() }
		ButtonAnimator(settingsButton).animateStrongPressing()
		
		errorTextView = binding.coreScheduleErrorTextview
		
		//Subscribing on viewModel's fields
		viewModel.lessonsViews?.subscribe {
			if (it == null) placeWaitingSpinner()
			else placeSchedule(it)
		}
		viewModel.selectedWeekParity?.subscribe {
			when(it) {
				ScheduleWeekParity.Even -> setWeekEven()
				ScheduleWeekParity.Odd -> setWeekOdd()
			}
		}
		viewModel.selectedPage?.subscribe(::goToPage)
		viewModel.errorMessage?.subscribe {
			if (it == null) removeErrorMessage() else makeErrorMessage(it)
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated()
	}

	private fun placeSchedule(schedule: Array<Fragment>) {
		ViewCompat.animate(viewPager)
			.alpha(0.0f)
			.setInterpolator(AccelerateDecelerateInterpolator())
			.setDuration(300)
			.withEndAction {
				val adapter = SchedulePagerAdapter(this, schedule)
				viewPager.adapter = adapter
				viewPager.offscreenPageLimit = 7
				ViewCompat.animate(viewPager)
					.alpha(1f)
					.setInterpolator(AccelerateDecelerateInterpolator())
					.setDuration(300)
					.start()
			}
			.start()
	}

	private fun placeWaitingSpinner() {
		viewModel.onWaitingSpinnerPlacing()
		placeSchedule(arrayOf(ScheduleWaitingFragment()))
	}
	
	private fun goToPage(index: Int) {
		viewPager.adapter?.let { if (index < it.itemCount) viewPager.currentItem = index }
	}
	
	private fun setWeekEven() {
		weekParityButton.text = resources.getString(R.string.even)
		weekParityButton.setTextColor(resources.getColor(R.color.colorBlue, activity?.theme))
		weekParityButton.background?.setTint(resources.getColor(R.color.colorBlueLight, activity?.theme))
	}
	
	private fun setWeekOdd() {
		weekParityButton.text = resources.getString(R.string.odd)
		weekParityButton.setTextColor(resources.getColor(R.color.colorDeadlineRed, activity?.theme))
		weekParityButton.background?.setTint(resources.getColor(R.color.colorRedMainLight, activity?.theme))
	}
	
	private fun makeErrorMessage(stringId: Int) {
		ViewCompat.animate(viewPager)
			.alpha(0f)
			.setInterpolator(AccelerateDecelerateInterpolator())
			.setDuration(150)
			.withEndAction {
				viewPager.visibility = View.INVISIBLE
				errorTextView.alpha = 0f
				errorTextView.text = getString(stringId)
				errorTextView.visibility = View.VISIBLE
				ViewCompat.animate(errorTextView)
					.alpha(1f)
					.setInterpolator(AccelerateDecelerateInterpolator())
					.setDuration(150)
					.start()
			}
			.start()
	}
	
	private fun removeErrorMessage() {
		if (errorTextView.visibility != View.GONE) {
			ViewCompat.animate(errorTextView)
				.alpha(0f)
				.setInterpolator(AccelerateDecelerateInterpolator())
				.setDuration(150)
				.withEndAction {
					errorTextView.visibility = View.GONE
					viewPager.visibility = View.VISIBLE
					ViewCompat.animate(viewPager)
						.alpha(1f)
						.setInterpolator(AccelerateDecelerateInterpolator())
						.setDuration(150)
						.start()
				}
				.start()
		}
	}
	
	override fun onDestroyView() {
		viewModel.lessonsViews?.unsubscribeAll()
		viewModel.selectedWeekParity?.unsubscribeAll()
		viewModel.selectedPage?.unsubscribeAll()
		viewModel.errorMessage?.unsubscribeAll()
		viewModel.onDestroyView()
		super.onDestroyView()
	}
}