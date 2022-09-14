package com.q2ve.schedappv2.ui.core.deadlines.pages

import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation
import com.q2ve.schedappv2.ui.AuthorizationRequirement
import com.q2ve.schedappv2.ui.popup.BottomPopupContainerFragment

class DeadlinesPageModuleBaseRouter {
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