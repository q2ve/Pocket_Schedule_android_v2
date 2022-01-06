package com.q2ve.pocketschedule2.helpers.recyclerSelector

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser

class RecyclerSelectorUploadingControllerProfessors(
	recyclerAdapter: RecyclerSelectorAdapter,
	private val onErrorCallback: (ErrorType) -> Unit,
	private var universityId: String
): RecyclerSelectorUploadingControllerBase(recyclerAdapter) {
	private var professors = emptyList<RealmItemScheduleUser>().toMutableList()
	
	fun getItem(index: Int): RealmItemScheduleUser? {
		return if (professors.size >= index + 1) professors[index] else null
	}
	
	override fun uploadItems() {
		if (offset == 0) {
			professors = emptyList<RealmItemScheduleUser>().toMutableList()
			emptyRecyclerDataset()
		}
		Model().getProfessors(offset, offset + paginationStep, universityId, query)
		{ objects, errorType ->
			if (errorType != null) {
				onErrorCallback(errorType)
			} else {
				professors += objects ?: emptyList()
				val stringsList = emptyList<String>().toMutableList()
				(objects ?: emptyList()).forEach { stringsList.add(it.name ?: "") }
				fillRecyclerDataset(stringsList)
			}
		}
	}
}