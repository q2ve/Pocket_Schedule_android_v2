package com.q2ve.pocketschedule2.ui.core.deadlines

import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.AuthorizationRequirement
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesRouter {
	fun openBottomPopupContainer(
		titleId: Int,
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null
	) {
		val fragment = BottomPopupContainerFragment.newInstance(titleId)
		fragment.buildObserver(onResumeCallback)
		Navigator.addFragment(
			fragment,
			Frames.getActivityFrame()!!,
			ReplaceAnimation.FadingWithoutScalingOnlyPopAnimation,
			true
		)
	}
	
	fun openAuthorizationRequirement() { AuthorizationRequirement().show(showBackButton = false) }
}