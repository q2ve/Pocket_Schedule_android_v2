package com.q2ve.schedappv2.ui.login

import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation
import com.q2ve.schedappv2.ui.login.login.LoginMethodSelectorFragment
import com.q2ve.schedappv2.ui.login.onboarding.OnboardingFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginNavigatorRouter {
	fun openOnboarding(goToEnd: Boolean = false) {
		val fragment = OnboardingFragment.newInstance(goToEnd)
		val frame = Frames.getLoginFrame()
		frame?.let {
			Navigator.replaceFragment(fragment, it, ReplaceAnimation.FadingWithoutScaling)
		}
	}
	
	fun openLoginMethodSelector() {
		val fragment = LoginMethodSelectorFragment()
		val frame = Frames.getLoginFrame()
		frame?.let {
			Navigator.replaceFragment(fragment, it, ReplaceAnimation.FadingWithoutScaling)
		}
	}
}