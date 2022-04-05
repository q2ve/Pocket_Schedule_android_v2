package com.q2ve.pocketschedule2.ui.login

import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.helpers.Observable

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginNavigatorViewModel: ViewModel() {
	lateinit var startScreen: LoginScreens
	
	private val router = LoginNavigatorRouter()
	
	var ellipseViewModel: Observable<BackgroundEllipseViewModel>? = null
	
	fun onCreateView() {
		BackgroundEllipseObservable.subscribe(::moveEllipse)
		ellipseViewModel = Observable(BackgroundEllipseProperties.onboardingPages[0])
	}
	
	fun onViewCreated() {
		when (startScreen) {
			LoginScreens.Default -> router.openOnboarding()
			LoginScreens.Onboarding -> router.openOnboarding()
			LoginScreens.OnboardingEnd -> router.openOnboarding(goToEnd = true)
			LoginScreens.First -> router.openLoginMethodSelector()
		}
	}
	
	private fun moveEllipse(properties: BackgroundEllipseViewModel) {
		ellipseViewModel?.value = properties
	}
	
	fun onDestroyView() {
		//TODO("Проверить, нужно ли это")
		BackgroundEllipseObservable.unsubscribe(::moveEllipse)
		ellipseViewModel = null
	}
}