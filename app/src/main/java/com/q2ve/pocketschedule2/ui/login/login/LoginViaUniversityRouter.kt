package com.q2ve.pocketschedule2.ui.login.login

import android.app.Activity
import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.core.CoreNavigatorFragment
import com.q2ve.pocketschedule2.ui.scheduleUserPicker.ScheduleUserPickerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginViaUniversityRouter {
	
	fun openScheduleUserPicker() {
		Frames.getActivityFrame()?.let { frame: Int ->
			Navigator.replaceFragment(
				ScheduleUserPickerFragment.newInstance(false),
				frame,
				ReplaceAnimation.SlideRtL,
				true
			)
		}
		Navigator.clearBackstack()
	}
	
	fun goBackToLoginMethodSelector(activity: Activity) { activity.onBackPressed() }
	
	fun goToCoreFragments() {
		Frames.getActivityFrame()?.let { frame: Int ->
			Navigator.replaceFragment(
				CoreNavigatorFragment.newInstance(),
				frame,
				ReplaceAnimation.SlideDownBounce,
				false
			)
		}
		Navigator.clearBackstack()
	}
}