package com.q2ve.schedappv2.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.q2ve.schedappv2.databinding.AuthorizationRequirementBinding
import com.q2ve.schedappv2.helpers.ButtonAnimator
import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.allowBackButtonAction
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation
import com.q2ve.schedappv2.ui.login.LoginNavigatorFragment
import com.q2ve.schedappv2.ui.login.LoginScreens

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */
class AuthorizationRequirement: Fragment() {
	
	fun show(showBackButton: Boolean) {
		Frames.getActivityFrame()?.let {
			this.arguments = bundleOf("showBackButton" to showBackButton)
			Navigator.addFragment(
				this, it, ReplaceAnimation.FadingWithoutScaling, true
			)
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		allowBackButtonAction(false)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = AuthorizationRequirementBinding.inflate(inflater, container, false)
		
		val showBackButton = arguments?.getBoolean("showBackButton") ?: false
		Log.e("AuthorizationRequirement.arguments", arguments.toString())
		
		val enterButton = binding.authorizationRequirementEnterButton
		val backButton = binding.authorizationRequirementBackButton
		
		enterButton.setOnClickListener {
			val frame = Frames.getActivityFrame()
			frame?.let {
				Navigator.replaceFragment(
					LoginNavigatorFragment.newInstance(LoginScreens.OnboardingEnd),
					it,
					ReplaceAnimation.FadingWithoutScaling,
					false
				)
				Navigator.clearBackstack()
			}
		}
		ButtonAnimator(enterButton).animateWeakPressing()
		
		if (showBackButton) {
			allowBackButtonAction(true)
			backButton.visibility = View.VISIBLE
			backButton.setOnClickListener {
				Navigator.removeFragment(this, ReplaceAnimation.FadingWithoutScaling)
			}
		}
		ButtonAnimator(backButton).animateStrongPressing()
		
		return binding.root
	}
	
	override fun onDestroy() {
		allowBackButtonAction(true)
		super.onDestroy()
	}
}