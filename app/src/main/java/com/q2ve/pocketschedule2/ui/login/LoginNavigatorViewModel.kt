package com.q2ve.pocketschedule2.ui.login

import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.helpers.Observable

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("А как будет получаться доступ к управдению эллипсом из других фрагментов? Подумать о синглтоне с подпиской")
class LoginNavigatorViewModel: ViewModel() {
	lateinit var startScreen: LoginScreens
	
	private val router = LoginNavigatorRouter()
	
	var ellipseTranslationX: Observable<Float>? = Observable(0f)
	var ellipseTranslationY: Observable<Float>? = Observable(0f)
	var ellipseRotation: Observable<Float>? = Observable(0f)
	
	fun viewCreated() {
		when (startScreen) {
			LoginScreens.Default -> router.openOnboarding()
			LoginScreens.Onboarding -> TODO()
			LoginScreens.OnboardingEnd -> TODO()
			LoginScreens.First -> TODO()
		}
	}
	
	fun moveEllipse(translationX: Float, translationY: Float, rotation: Float) {
		ellipseTranslationX?.value = translationX
		ellipseTranslationY?.value = translationY
		ellipseRotation?.value = rotation
	}
	
	fun onDestroyView() {
		//TODO("Проверить, нужно ли это")
		ellipseTranslationX = null
		ellipseTranslationY = null
		ellipseRotation = null
	}
}