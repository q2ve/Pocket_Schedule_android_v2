package com.q2ve.pocketschedule2.ui.core.schedule

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.AuthorizationRequirement
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment
import com.q2ve.pocketschedule2.ui.scheduleUserPicker.ScheduleUserPickerFragment
import com.q2ve.pocketschedule2.ui.settings.SettingsFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleRouter {
	fun openBottomPopupContainer(
		titleId: Int,
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null,
		onCloseCallback: (() -> Unit)? = null
	) {
		val fragment = BottomPopupContainerFragment.newInstance(titleId)
		val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
			if (event == Lifecycle.Event.ON_RESUME) onResumeCallback?.let { it(fragment) }
			if (event == Lifecycle.Event.ON_DESTROY) onCloseCallback?.let { it() }
		}
		fragment.lifecycle.addObserver(observer)
		Navigator.addFragment(fragment, Frames.getActivityFrame()!!, null, true)
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