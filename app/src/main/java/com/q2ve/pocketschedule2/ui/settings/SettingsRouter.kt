package com.q2ve.pocketschedule2.ui.settings

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.ui.AuthorizationRequirement
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class SettingsRouter {
	fun openBottomPopupContainer(
		titleId: Int,
		onResumeCallback: ((BottomPopupContainerFragment) -> Unit)? = null,
		onCloseCallback: (() -> Unit)? = null
	) {
		Frames.getActivityFrame()?.let { frame ->
			val fragment = BottomPopupContainerFragment.newInstance(titleId)
			val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
				if (event == Lifecycle.Event.ON_RESUME) onResumeCallback?.let { it(fragment) }
				if (event == Lifecycle.Event.ON_DESTROY) onCloseCallback?.let { it() }
			}
			fragment.lifecycle.addObserver(observer)
			Navigator.addFragment(fragment, frame, null, true)
		}
	}
	
	fun goBack(activity: Activity) { activity.onBackPressed() }
	
	fun openAuthorizationRequirement() { AuthorizationRequirement().show(showBackButton = false) }
}