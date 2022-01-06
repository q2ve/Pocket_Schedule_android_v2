package com.q2ve.pocketschedule2.helpers.recyclerSelector

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser

class RecyclerSelectorUploadingControllerGroups(
	recyclerAdapter: RecyclerSelectorAdapter,
	private val onErrorCallback: (ErrorType) -> Unit,
	private var universityId: String
): RecyclerSelectorUploadingControllerBase(recyclerAdapter) {
	private var groups = emptyList<RealmItemScheduleUser>().toMutableList()
	
	fun getItem(index: Int): RealmItemScheduleUser? {
		return if (groups.size >= index + 1) groups[index] else null
	}
	
	override fun uploadItems() {
		if (offset == 0) {
			groups = emptyList<RealmItemScheduleUser>().toMutableList()
			emptyRecyclerDataset()
		}
		Model().getGroups(offset, offset + paginationStep, universityId, query)
		{ objects, errorType ->
			if (errorType != null) {
				onErrorCallback(errorType)
			} else {
				groups += objects ?: emptyList()
				val stringsList = emptyList<String>().toMutableList()
				(objects ?: emptyList()).forEach { stringsList.add(it.name ?: "") }
				fillRecyclerDataset(stringsList)
			}
		}
	}
}