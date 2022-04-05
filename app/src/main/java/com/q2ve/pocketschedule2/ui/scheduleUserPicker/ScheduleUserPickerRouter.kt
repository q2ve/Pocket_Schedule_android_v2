package com.q2ve.pocketschedule2.ui.scheduleUserPicker

import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.core.CoreNavigatorFragment
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleUserPickerRouter {
	fun goToCoreFragments() {
		val fragment = CoreNavigatorFragment.newInstance()
		val frame = Frames.getActivityFrame()
		frame?.let {
			Navigator.replaceFragment(
				fragment,
				frame,
				ReplaceAnimation.SlideUpBounce,
				false
			)
		}
	}
	
	fun openBottomPopupContainer(
		titleId: Int,
		openOnFullScreen: Boolean,
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null
	) {
		Frames.getActivityFrame()?.let { frame ->
			val fragment = BottomPopupContainerFragment.newInstance(titleId, openOnFullScreen)
			fragment.buildObserver(onResumeCallback)
			Navigator.addFragment(fragment, frame, null, true)
		}
	}
	
	fun goBack() {
		//TODO()
	}
}