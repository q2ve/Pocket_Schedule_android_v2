package com.q2ve.pocketschedule2.ui.login

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

private typealias Properties = BackgroundEllipseViewModel

object BackgroundEllipseProperties {
	val onboardingPages = listOf(
		Properties(-65F, 170F, -280F),
		Properties(-65F, -150F, 110F),
		Properties(60F, -165F, -120F),
		Properties(-50F, -60F, -20F),
		Properties(55F, 80F, 120F),
	)
	val loginMethodSelector = Properties(60F, 80F, 70F)
	val loginViaUniversity = Properties(40F, -80F, 70F)
	val scheduleUserPicker = Properties(60F, -320F, 20F)
}
