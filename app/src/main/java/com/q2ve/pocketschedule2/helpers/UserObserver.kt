package com.q2ve.pocketschedule2.helpers

import com.q2ve.pocketschedule2.helpers.navigator.Navigator
import com.q2ve.pocketschedule2.helpers.navigator.ReplaceAnimation
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemMain
import com.q2ve.pocketschedule2.model.realm.RealmIO
import com.q2ve.pocketschedule2.ui.LogoutNotification
import com.q2ve.pocketschedule2.ui.login.LoginNavigatorFragment
import com.q2ve.pocketschedule2.ui.login.LoginScreens

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

object UserObserver {
	private lateinit var mainObject: RealmItemMain
	
	fun configure() {
		setMainObject(RealmIO().getMainObject())
		RealmIO().observeMainObject(::setMainObject)
	}
	
	private fun setMainObject(newMainObject: RealmItemMain) { mainObject = newMainObject }
	
	fun getMainObject() = mainObject
	
	fun logoutUser(showNotification: Boolean = true) {
		val sessionId = mainObject.sessionId
		if (sessionId != null) Model{ }.postLogout(sessionId)
		RealmIO().resetMainObject()
		if (showNotification) LogoutNotification().show() else {
			val fragment = LoginNavigatorFragment.newInstance(LoginScreens.OnboardingEnd)
			val frame = Frames.getActivityFrame()
			val animation = ReplaceAnimation.FadingWithoutScaling
			frame?.let {
				Navigator.replaceFragment(fragment, it, animation, false)
				Navigator.clearBackstack()
			}
		}
	}
}