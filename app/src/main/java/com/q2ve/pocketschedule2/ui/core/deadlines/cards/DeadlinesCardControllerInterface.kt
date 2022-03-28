package com.q2ve.pocketschedule2.ui.core.deadlines.cards

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline

interface DeadlinesCardControllerInterface {
	fun openDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	)
	fun closeDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	)
	fun editDeadline(
		deadline: RealmItemDeadline,
		inflater: LayoutInflater,
		viewContainer: ViewGroup,
		resources: Resources,
		theme: Resources.Theme,
		closeCard: ((RealmItemDeadline?) -> Unit)? = null
	)
	fun deleteDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	)
	fun createDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	)
	fun updateDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	)
}