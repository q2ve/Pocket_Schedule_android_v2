package com.q2ve.pocketschedule2.ui.login.onboarding

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.q2ve.pocketschedule2.R

class OnboardingPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
	override fun getItemCount(): Int = 5

	override fun createFragment(position: Int): Fragment {

		return when (position) {
			0 -> {
				OnboardingPageOneFragment()
			}
			1 -> {
				OnboardingPageTwoFragment()
			}
			2 -> {
				OnboardingPageThreeFragment()
			}
			3 -> {
				OnboardingPageFourFragment()
			}
			4 -> {
				OnboardingPageFiveFragment()
			}
			else -> {
				OnboardingPageOneFragment()
			}
		}
	}

}