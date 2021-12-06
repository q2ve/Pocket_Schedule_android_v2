package com.q2ve.pocketschedule2.ui.login

import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.Frames
import com.q2ve.pocketschedule2.ui.login.onboarding.OnboardingFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginNavigatorRouter {
	fun openOnboarding() {
		val fragment = OnboardingFragment.newInstance()
		val frame = Frames.getLoginFrame()
		if (frame != null) {
			Navigator.replaceFragment(fragment, frame, ReplaceAnimation.FadingWithoutScaling)
		} else {
			TODO("Not yet implemented")
		}
	}
}

//takzhe