package com.q2ve.schedappv2.ui.login.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.q2ve.schedappv2.databinding.OnboardingHostBinding
import com.q2ve.schedappv2.ui.login.BackgroundEllipseObservable
import com.q2ve.schedappv2.ui.login.BackgroundEllipseProperties

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class OnboardingFragment: Fragment() {
	companion object {
		fun newInstance(goToEnd: Boolean = false): Fragment {
			val fragment = OnboardingFragment()
			fragment.arguments = bundleOf(
				"goToEnd" to goToEnd
			)
			return fragment
		}
	}
	
	private lateinit var viewPager: ViewPager2
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = OnboardingHostBinding.inflate(inflater, container, false)
		
		val adapter = OnboardingPagerAdapter(this)
		viewPager = binding.loginOnboardingPager
		viewPager.adapter = adapter
		viewPager.offscreenPageLimit = 5
		viewPager.setPageTransformer { page, position ->
			page.apply {
				val r = 1 - kotlin.math.abs(position)
				page.alpha = 0.25f + r
				page.scaleY = 0.75f + r * 0.25f
				
			}
		}
		val goToEnd = arguments?.getBoolean("goToEnd") == true
		if (goToEnd) {
			viewPager.setCurrentItem(adapter.itemCount - 1, false)
		}
		
		registerOnPageChangeCallback()
		
		return binding.root
	}
	
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt("selectedPage", viewPager.currentItem)
	}
	
	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		if (savedInstanceState != null) {
			//TODO("Не работает, починить.")
			viewPager.setCurrentItem(savedInstanceState.getInt("selectedPage"), false)
		}
	}
	
	private fun registerOnPageChangeCallback() {
		viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				val ellipseViewModel = BackgroundEllipseProperties.onboardingPages[position]
				BackgroundEllipseObservable.changeProperties(ellipseViewModel)
			}
		})
	}
}