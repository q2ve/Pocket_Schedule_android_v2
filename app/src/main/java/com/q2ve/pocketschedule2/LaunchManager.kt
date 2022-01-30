package com.q2ve.pocketschedule2

import androidx.fragment.app.FragmentActivity
import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.core.CoreNavigatorFragment
import io.realm.Realm

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LaunchManager(private val activity: FragmentActivity) {
	
	fun initializeHelpers() {
		//ResourceGetter.configure(activity)
		Navigator.configure(activity.supportFragmentManager)
		Realm.init(activity)
		UserObserver.configure()
	}
	
	fun applicationStarted() {
		val fragment = CoreNavigatorFragment.newInstance()
		//val fragment = LoginNavigatorFragment.newInstance()
		val frame = Frames.getActivityFrame()
		frame?.let {
			Navigator.replaceFragment(
				fragment,
				frame,
				ReplaceAnimation.FadingWithoutScaling,
				false
			)
		}
	}
}

//TODO("отдельная функия выбора core/login навигатора. С интерфейсом")