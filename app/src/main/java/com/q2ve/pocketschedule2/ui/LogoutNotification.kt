package com.q2ve.pocketschedule2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.databinding.LogoutNotificationBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator
import com.q2ve.pocketschedule2.helpers.Frames
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.helpers.allowBackButtonAction
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LogoutNotification: Fragment() {
	
	fun show() {
		Frames.getActivityFrame()?.let {
			Navigator.addFragment(this, it, ReplaceAnimation.FadingWithoutScaling)
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
		val binding = LogoutNotificationBinding.inflate(inflater, container, false)
		
		val enterButton = binding.logoutNotificationEnterButton
		enterButton.setOnClickListener { UserObserver.logoutUser(false) }
		ButtonAnimator(enterButton).animateWeakPressing()
		
		return binding.root
	}
	
	override fun onDestroy() {
		allowBackButtonAction(true)
		super.onDestroy()
	}
}