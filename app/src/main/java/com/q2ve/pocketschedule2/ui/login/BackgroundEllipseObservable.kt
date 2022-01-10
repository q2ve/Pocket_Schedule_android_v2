package com.q2ve.pocketschedule2.ui.login

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

object BackgroundEllipseObservable {
	private var callback: ((BackgroundEllipseViewModel) -> Unit)? = null
	
	private var properties = BackgroundEllipseProperties.onboardingPages[0]
		set(value) {
			field = value
			callback?.invoke(properties)
		}
	
	fun changeProperties(viewModel: BackgroundEllipseViewModel) {
		this.properties = viewModel
	}
	
	fun subscribe(observer: (BackgroundEllipseViewModel) -> Unit) {
		callback = observer
	}
	
	fun unsubscribe(observer: (BackgroundEllipseViewModel) -> Unit) {
		callback = null
	}
}