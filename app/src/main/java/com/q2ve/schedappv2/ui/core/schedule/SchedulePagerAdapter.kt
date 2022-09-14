package com.q2ve.schedappv2.ui.core.schedule

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SchedulePagerAdapter(
	fragment: Fragment,
	private val scheduleDays: Array<Fragment>
): FragmentStateAdapter(fragment) {
	override fun getItemCount(): Int = scheduleDays.size
	override fun createFragment(position: Int): Fragment = scheduleDays[position]
}