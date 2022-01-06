package com.q2ve.pocketschedule2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.databinding.OutdatedAppNotificationBinding
import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class OutdatedAppNotification: Fragment() {
	
	fun show() {
		Frames.getActivityFrame()?.let {
			Navigator.addFragment(this, it, ReplaceAnimation.FadingWithoutScaling)
		}
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = OutdatedAppNotificationBinding.inflate(inflater, container, false)
		
		val button = binding.outdatedAppNotificationButton
		
		button.setOnClickListener {
			//TODO()
			Toast.makeText(context, "Ссылку на страницу в плеймаркете пока не завезли :(", Toast.LENGTH_SHORT).show()
		}
		ButtonAnimator(button)
		
		return binding.root
	}
}