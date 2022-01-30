package com.q2ve.pocketschedule2.ui.core

import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.core.schedule.ScheduleFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class CoreNavigatorRouter {
	fun openNews() {
		//TODO("Not yet implemented")
	}
	
	fun openSchedule() {
		val fragment = ScheduleFragment()
		val frame = Frames.getCoreFrame()
		frame?.let {
			Navigator.replaceFragment(
				fragment,
				frame,
				ReplaceAnimation.FadingWithoutScaling,
				false
			)
			Navigator.clearBackstack()
		}
	}
	
	fun openDeadlines() {
		//TODO("Not yet implemented")
	}
	
	fun openAuthorizationRequirement() {
		//TODO("Not yet implemented")
	}
}