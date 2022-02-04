package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesPageModuleExternal(
	emptyLayoutId: Int,
	private val sessionId: String,
	private val deadlineSourceId: String,
	onDeadlineClicked: (RealmItemDeadline) -> Unit,
	onDeadlineCheckboxClicked: (RealmItemDeadline) -> Unit,
	private val onError: (ErrorType) -> Unit
): DeadlinesPageModuleBase(emptyLayoutId, onDeadlineClicked, onDeadlineCheckboxClicked) {
	override fun getDeadlines() {
		Model(onError).getExternalDeadlines(sessionId, deadlineSourceId, ::inflateDeadlines)
	}
}