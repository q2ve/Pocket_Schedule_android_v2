package com.q2ve.schedappv2.ui.core.schedule

import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation
import com.q2ve.schedappv2.ui.AuthorizationRequirement
import com.q2ve.schedappv2.ui.popup.BottomPopupContainerFragment
import com.q2ve.schedappv2.ui.scheduleUserPicker.ScheduleUserPickerFragment
import com.q2ve.schedappv2.ui.settings.SettingsFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleRouter {
	fun openBottomPopupContainer(
		titleId: Int,
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null
	) {
		val fragment = BottomPopupContainerFragment.newInstance(titleId, true)
		fragment.buildObserver(onResumeCallback)
		Navigator.addFragment(
			fragment,
			Frames.getActivityFrame()!!,
			ReplaceAnimation.FadingWithoutScalingOnlyPopAnimation,
			true
		)
	}
	
	fun openSettings() {
		Frames.getActivityFrame()?.let { frame: Int ->
			Navigator.replaceFragment(
				SettingsFragment(),
				frame,
				ReplaceAnimation.SlideUpBounce,
				true
			)
			Navigator.clearBackstack()
		}
	}
	
	fun openAuthorizationRequirement() { AuthorizationRequirement().show(showBackButton = true) }
	
	fun openScheduleUserPicker() {
		Frames.getActivityFrame()?.let { frame: Int ->
			Navigator.replaceFragment(
				ScheduleUserPickerFragment.newInstance(false),
				frame,
				ReplaceAnimation.SlideDownBounce,
				false
			)
			Navigator.clearBackstack()
		}
	}
}