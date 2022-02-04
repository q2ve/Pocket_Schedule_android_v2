package com.q2ve.pocketschedule2.ui.core.deadlines

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
import com.q2ve.pocketschedule2.databinding.DeadlinesBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator
import com.q2ve.pocketschedule2.ui.core.deadlines.pages.DeadlinesPageBase
import com.q2ve.pocketschedule2.ui.core.deadlines.pages.DeadlinesPageLoadingDummy
import com.q2ve.pocketschedule2.ui.core.deadlines.pages.DeadlinesPagerAdapter

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesFragment: Fragment() {
	private lateinit var viewModel: DeadlinesViewModelInterface
	private lateinit var viewPager: ViewPager2
	private lateinit var errorTextView: TextView
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = DeadlinesBinding.inflate(inflater, container, false)
		
		viewModel = ViewModelProvider(this).get(DeadlinesViewModel::class.java)
		viewModel.onCreateView(
			inflater,
			binding.coreDeadlinesSourcesFrame,
			resources,
			requireActivity().theme
		)
		
		//ViewPager
		viewPager = binding.coreDeadlinesModulePager
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
		val addButton = binding.coreDeadlinesAddButton
		addButton.setOnClickListener { viewModel.onAddButtonPressed() }
		ButtonAnimator(addButton).animateStrongPressing()
		
		errorTextView = binding.coreDeadlinesErrorTextview
		
		//Subscribing on viewModel's fields
		viewModel.deadlinePages?.subscribe {
			if (it == null) placeWaitingSpinner()
			else placePages(it)
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
	
	private fun placePages(deadlinesScreens: List<DeadlinesPageBase>) {
		ViewCompat.animate(viewPager)
			.alpha(0.0f)
			.setInterpolator(AccelerateDecelerateInterpolator())
			.setDuration(300)
			.withEndAction {
				val adapter = DeadlinesPagerAdapter(viewPager)
				adapter.pages = deadlinesScreens
				viewPager.adapter = adapter
				viewPager.offscreenPageLimit = 3
				ViewCompat.animate(viewPager)
					.alpha(1f)
					.setInterpolator(AccelerateDecelerateInterpolator())
					.setDuration(300)
					.start()
			}
			.start()
	}
	
	private fun placeWaitingSpinner() { placePages(listOf(DeadlinesPageLoadingDummy())) }
	
	private fun goToPage(index: Int) {
		viewPager.adapter?.let { if (index < it.itemCount) viewPager.currentItem = index }
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
		viewModel.deadlinePages?.unsubscribeAll()
		viewModel.selectedPage?.unsubscribeAll()
		viewModel.errorMessage?.unsubscribeAll()
		viewModel.onDestroyView()
		super.onDestroyView()
	}
}