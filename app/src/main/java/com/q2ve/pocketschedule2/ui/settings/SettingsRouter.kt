package com.q2ve.pocketschedule2.ui.settings

import android.app.Activity
import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.AuthorizationRequirement
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class SettingsRouter {
	fun openBottomPopupContainer(
		titleId: Int,
		openOnFullScreen: Boolean,
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null
	) {
		Frames.getActivityFrame()?.let { frame ->
			val fragment = BottomPopupContainerFragment.newInstance(titleId, openOnFullScreen)
			fragment.buildObserver(onResumeCallback)
			Navigator.addFragment(
				fragment,
				frame,
				ReplaceAnimation.FadingWithoutScalingOnlyPopAnimation,
				true
			)
		}
	}
	
	fun goBack(activity: Activity) { activity.onBackPressed() }
	
	fun openAuthorizationRequirement() { AuthorizationRequirement().show(showBackButton = false) }
}