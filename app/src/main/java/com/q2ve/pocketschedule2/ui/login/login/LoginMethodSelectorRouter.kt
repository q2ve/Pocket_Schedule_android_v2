package com.q2ve.pocketschedule2.ui.login.login

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.ui.Frames
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginMethodSelectorRouter {
	fun openVKAuthScreen(activity: Activity) {
		VK.login(activity, arrayListOf(VKScope.WALL, VKScope.OFFLINE))
	}
	
	fun openUniversityAuthScreen() {
		//TODO()
	}
	
	fun openPopupScheduleUserSelector() {
		//TODO()
	}
	
	fun openBottomPopupContainer(
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null,
		onCloseCallback: (() -> Unit)? = null
	) {
		val titleId = R.string.university_choosing
		val fragment = BottomPopupContainerFragment.newInstance(titleId)
		val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
			if (event == Lifecycle.Event.ON_RESUME) onResumeCallback?.let { it(fragment) }
			if (event == Lifecycle.Event.ON_DESTROY) onCloseCallback?.let { it() }
		}
		fragment.lifecycle.addObserver(observer)
		Navigator.addFragment(fragment, Frames.getLoginFrame()!!, null, true)
	}
	
	fun goBackToOnboarding(activity: Activity) {
		activity.onBackPressed()
//		Frames.getLoginFrame()?.let { frame: Int ->
//			Navigator.replaceFragment(
//				OnboardingFragment.newInstance(true),
//				frame,
//				ReplaceAnimation.SlideLtR,
//				true
//			)
//		}
	}
	
	fun goToCoreFragments() {
		//TODO()
	}
}