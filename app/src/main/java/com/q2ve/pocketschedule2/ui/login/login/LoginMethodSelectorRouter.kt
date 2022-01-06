package com.q2ve.pocketschedule2.ui.login.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.ui.Frames
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginMethodSelectorRouter {
	fun openVKAuthScreen() {
	
	}
	
	fun openUniversityAuthScreen() {
	
	}
	
	fun openUniversitySelector(
		onResumeCallback: (BottomPopupContainerFragment) -> Unit,
		onCloseCallback: () -> Unit
	) {
		val titleId = R.string.university_choosing
		val fragment = BottomPopupContainerFragment.newInstance(titleId)
		val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
			if (event == Lifecycle.Event.ON_RESUME) onResumeCallback(fragment)
			if (event == Lifecycle.Event.ON_DESTROY) onCloseCallback()
		}
		fragment.lifecycle.addObserver(observer)
		Navigator.addFragment(fragment, Frames.getLoginFrame()!!)
	}
	
	fun goBackToOnboarding() {
	
	}
}