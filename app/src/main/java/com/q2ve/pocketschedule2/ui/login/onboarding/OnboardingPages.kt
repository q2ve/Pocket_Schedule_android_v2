package com.q2ve.pocketschedule2.ui.login.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.Onboarding5Binding
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.ui.ButtonAnimator
import com.q2ve.pocketschedule2.ui.Frames
import com.q2ve.pocketschedule2.ui.login.login.LoginMethodSelectorFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Animated presses on onboarding examples. Maybe with something that will opens")
class OnboardingPageOneFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val rootView = inflater.inflate(R.layout.onboarding_1, container, false)
		
		//val logo = rootView.login_onboarding_1_logo
		//ButtonAnimator(logo)
		//logo.setOnClickListener {
		//   TODO("Some cool action")
		//}
		//val logoUp = rootView.login_onboarding_1_logo_up
		//val logoDown = rootView.login_onboarding_1_logo_down
		
		return rootView
	}
}

class OnboardingPageTwoFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val rootView = inflater.inflate(R.layout.onboarding_2, container, false)
		
		//val example = rootView.onboarding_2_example
		//ButtonAnimator(example)
		//example.setOnClickListener {
		//   TODO("Some cool action")
		//}
		
		return rootView
	}
}

class OnboardingPageThreeFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val rootView = inflater.inflate(R.layout.onboarding_3, container, false)
		
		//val example1 = rootView.onboarding_3_example_1
		//val example2 = rootView.onboarding_3_example_2
		//val example3 = rootView.onboarding_3_example_3
		//ButtonAnimator(example1)
		//ButtonAnimator(example2)
		//ButtonAnimator(example3)
		//example1.setOnClickListener {
		//   TODO("Some cool action")
		//}
		//example2.setOnClickListener {
		//   TODO("Some cool action")
		//}
		//example3.setOnClickListener {
		//   TODO("Some cool action")
		//}
		
		return rootView
	}
}

class OnboardingPageFourFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val rootView = inflater.inflate(R.layout.onboarding_4, container, false)
		
		//val example1 = rootView.onboarding_4_example_1
		//val example2 = rootView.onboarding_4_example_2
		//val example3 = rootView.onboarding_4_example_3
		//ButtonAnimator(example1)
		//ButtonAnimator(example2)
		//ButtonAnimator(example3)
		//example1.setOnClickListener {
		//   TODO("Some cool action")
		//}
		//example2.setOnClickListener {
		//   TODO("Some cool action")
		//}
		//example3.setOnClickListener {
		//   TODO("Some cool action")
		//}
		
		return rootView
	}
}

class OnboardingPageFiveFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = Onboarding5Binding.inflate(inflater, container, false)
		
		val router = OnboardingPageFiveRouter()
		
		val authoriseButton = binding.loginOnboardingAuthorizeButton
		ButtonAnimator(authoriseButton).animateWeakPressing()
		authoriseButton.setOnClickListener {
			router.goToLogin()
		}
		
		val noAuthoriseButton = binding.loginOnboardingNoAuthorizeButton
		ButtonAnimator(noAuthoriseButton).animateWeakPressing()
		noAuthoriseButton.setOnClickListener {
			router.openScheduleUserSelector()
		}
		
		return binding.root
	}
}

class OnboardingPageFiveRouter {
	fun goToLogin() {
		val frame = Frames.getLoginFrame()
		if (frame != null) {
			Navigator.replaceFragment(
				LoginMethodSelectorFragment(),
				frame,
				ReplaceAnimation.SlideRtL,
				true
			)
		}
	}
	
	fun openScheduleUserSelector() {
	
	}
}