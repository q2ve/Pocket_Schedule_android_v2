package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.q2ve.pocketschedule2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesPageLoadingDummy: DeadlinesPageBase() {
	override fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		position: Int,
		viewUpdatingFunction: (Int) -> Unit
	): View {
		return inflater.inflate(R.layout.deadlines_loader_dummy, container, false)
	}
}