package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesPageModuleExternal(
	emptyLayoutId: Int,
	private val sessionId: String,
	private val deadlineSourceId: String,
	onError: (ErrorType) -> Unit
): DeadlinesPageModuleBase(emptyLayoutId) {
	private val model = Model(onError)
	
	override fun getDeadlines() {
		model.getExternalDeadlines(sessionId, deadlineSourceId, ::inflateDeadlines)
	}
}