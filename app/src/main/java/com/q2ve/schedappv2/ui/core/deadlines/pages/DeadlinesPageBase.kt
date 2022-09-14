package com.q2ve.schedappv2.ui.core.deadlines.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Replace with interface?.")
abstract class DeadlinesPageBase {
	abstract fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		position: Int,
		viewUpdatingCall: (Int) -> Unit
	): View
	
	open fun onDestroyView() = Unit
}