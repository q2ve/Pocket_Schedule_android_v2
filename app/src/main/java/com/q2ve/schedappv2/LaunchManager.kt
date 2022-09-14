package com.q2ve.schedappv2

import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.UserObserver
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation
import com.q2ve.schedappv2.ui.core.CoreNavigatorFragment
import com.q2ve.schedappv2.ui.login.LoginNavigatorFragment
import io.realm.Realm

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LaunchManager(private val activity: FragmentActivity, private val logo: View) {
	private lateinit var startingFragment: Fragment
	
	fun initializeHelpers() {
		Navigator.configure(activity.supportFragmentManager)
		Realm.init(activity)
		UserObserver.configure()
	}
	
	fun applicationStarted() {
		startingFragment = getStartingFragment()
		hideLogo(logo, ::placeStartingFragment)
	}
	
	private fun getStartingFragment(): Fragment {
		val mainObject = UserObserver.getMainObject()
		return if (mainObject.scheduleUser == null || mainObject.scheduleUniversity == null) {
			LoginNavigatorFragment.newInstance()
		} else CoreNavigatorFragment.newInstance()
	}
	
	private fun hideLogo(logo: View, onHideCallback: () -> Unit) {
		val defaultAlpha = logo.alpha
		ViewCompat.animate(logo)
			.alpha(0f)
			.setDuration(400)
			.setStartDelay(400)
			.setInterpolator(AccelerateInterpolator())
			.withEndAction {
				logo.visibility = View.GONE
				logo.alpha = defaultAlpha
				onHideCallback()
				//Timer().schedule(timerTask { onHideCallback() }, 150)
			}
			.start()
	}
	
	private fun placeStartingFragment() {
		val frame = Frames.getActivityFrame()
		frame?.let {
			Navigator.replaceFragment(
				startingFragment,
				frame,
				ReplaceAnimation.FadingWithoutScaling,
				false
			)
		}
	}
}