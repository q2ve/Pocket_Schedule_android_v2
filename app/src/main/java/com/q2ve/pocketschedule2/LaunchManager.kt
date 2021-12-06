package com.q2ve.pocketschedule2

import androidx.fragment.app.FragmentActivity
import com.q2ve.pocketschedule2.helpers.ResourceGetter
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.Frames
import com.q2ve.pocketschedule2.ui.login.LoginNavigatorFragment
import io.realm.Realm

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LaunchManager(private val activity: FragmentActivity) {
	
	fun initializeHelpers() {
		ResourceGetter.configure(activity)
		Navigator.configure(activity)
		Realm.init(activity)
	}
	
	fun applicationStarted() {
		val fragment = LoginNavigatorFragment.newInstance()
		val frame = Frames.getActivityFrame()
		if (frame != null) {
			Navigator.replaceFragment(fragment, frame, ReplaceAnimation.FadingWithoutScaling)
		} else {
			TODO("Not yet implemented")
		}
	}
}

//TODO("отдельная функия выбора core/login навигатора. С интерфейсом")