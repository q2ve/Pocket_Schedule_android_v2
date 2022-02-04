package com.q2ve.pocketschedule2.ui.core.deadlines

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.q2ve.pocketschedule2.helpers.Observable
import com.q2ve.pocketschedule2.ui.core.deadlines.pages.DeadlinesPageBase

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

interface DeadlinesViewModelInterface {
	var deadlinePages: Observable<List<DeadlinesPageBase>?>?
	var selectedPage: Observable<Int>?
	var errorMessage: Observable<Int?>?
	
	fun onCreateView(
		inflater: LayoutInflater,
		buttonBarContainer: ViewGroup,
		resources: Resources,
		theme: Resources.Theme
	)
	fun onViewCreated()
	fun onPagerPageSelected(position: Int)
	fun onAddButtonPressed()
	fun onDestroyView()
}