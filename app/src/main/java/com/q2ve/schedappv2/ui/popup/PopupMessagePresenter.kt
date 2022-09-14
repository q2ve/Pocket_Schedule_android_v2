package com.q2ve.schedappv2.ui.popup

import android.os.Handler
import androidx.fragment.app.Fragment
import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class PopupMessagePresenter {
	fun createMessageSmall(isError: Boolean, stringResource: Int, ttl: Long = 4000) {
		val fragment = PopupMessageSmallFragment.newInstance(isError, stringResource, ttl)
		fragment.onViewCreatedCallback = ::onViewCreated
		Frames.getActivityFrame()?.let { frame: Int ->
			Navigator.addFragment(fragment, frame, ReplaceAnimation.SlideUpBounce)
		}
	}
	
	private fun onViewCreated(fragment: Fragment, ttl: Long) {
		val animation = ReplaceAnimation.SlideDownBounce
		Handler().postDelayed({ Navigator.removeFragment(fragment, animation) }, ttl)
	}
}