package com.q2ve.pocketschedule2.ui.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

@Parcelize
enum class LoginScreens: Parcelable {
	Default, Onboarding, OnboardingEnd, First
}