package com.q2ve.pocketschedule2.ui.core

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.ui.popup.PopupMessagePresenter

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class CoreNavigatorViewModel: ViewModel() {
	lateinit var startScreen: CoreScreens
	
	private val router = CoreNavigatorRouter()
	private lateinit var navbarController: NavbarController
	
	fun onCreateView(
		inflater: LayoutInflater,
		navbarContainer: ViewGroup,
		resources: Resources,
		theme: Resources.Theme
	) {
		val defaultColor = resources.getColor(R.color.colorLightGray1, theme)
		val selectionColor = resources.getColor(R.color.colorBlue, theme)
		navbarController = NavbarController(defaultColor, selectionColor)
		val navbar = navbarController.buildNavbar(
			inflater,
			navbarContainer,
			::onNewsButtonPressed,
			::onScheduleButtonPressed,
			::onDeadlinesButtonPressed
		)
		navbarContainer.addView(navbar)
	}
	
	fun onViewCreated() {
		val screen: CoreScreens = if (startScreen == CoreScreens.Default) {
			CoreScreens.values()[UserObserver.getMainObject().startScreen]
		} else startScreen
		when (screen) {
			CoreScreens.News -> {
				if (checkAuthorization()) {
					//TODO("Not yet implemented")
					router.openNews()
				} else router.openSchedule()
			}
			CoreScreens.Schedule -> {
				navbarController.scheduleSelected()
				router.openSchedule()
			}
			CoreScreens.Deadlines -> {
				if (checkAuthorization()) {
					navbarController.deadlinesSelected()
					router.openDeadlines()
				} else router.openSchedule()
			}
			else -> {
				navbarController.scheduleSelected()
				router.openSchedule()
			}
		}
	}
	
	private fun checkAuthorization(): Boolean {
		val mainObject = UserObserver.getMainObject()
		return (mainObject.currentUser != null && mainObject.sessionId != null)
	}
	
	private fun onNewsButtonPressed() {
		PopupMessagePresenter().createMessageSmall(false, R.string.coming_soon, 2000)
	}
	
	private fun onScheduleButtonPressed() {
		router.openSchedule()
		navbarController.scheduleSelected()
	}
	
	private fun onDeadlinesButtonPressed() {
		if (checkAuthorization()) {
			router.openDeadlines()
			navbarController.deadlinesSelected()
		}
		else router.openAuthorizationRequirement()
	}
}