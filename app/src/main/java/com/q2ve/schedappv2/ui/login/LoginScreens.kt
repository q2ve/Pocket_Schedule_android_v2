package com.q2ve.schedappv2.ui.login

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