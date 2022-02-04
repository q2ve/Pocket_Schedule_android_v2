package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

abstract class DeadlinesPageBase {
	abstract fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		position: Int,
		viewUpdatingFunction: (Int) -> Unit
	): View
}