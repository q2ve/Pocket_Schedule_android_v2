package com.q2ve.pocketschedule2.ui.login.login

import android.app.Activity
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.Frames
import com.q2ve.pocketschedule2.ui.scheduleUserPicker.ScheduleUserPickerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginViaUniversityRouter {
	
	fun openScheduleUserPicker() {
		Frames.getLoginFrame()?.let { frame: Int ->
			Navigator.replaceFragment(
				ScheduleUserPickerFragment.newInstance(false),
				frame,
				ReplaceAnimation.SlideRtL,
				true
			)
		}
	}
	
	fun goBackToLoginMethodSelector(activity: Activity) { activity.onBackPressed() }
	
	fun goToCoreFragments() {
		//TODO()
		openScheduleUserPicker()
	}
}