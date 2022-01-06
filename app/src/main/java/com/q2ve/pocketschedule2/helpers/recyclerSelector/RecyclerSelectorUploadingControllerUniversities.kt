package com.q2ve.pocketschedule2.helpers.recyclerSelector

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity

class RecyclerSelectorUploadingControllerUniversities(
	recyclerAdapter: RecyclerSelectorAdapter,
	private val onErrorCallback: (ErrorType) -> Unit
): RecyclerSelectorUploadingControllerBase(recyclerAdapter) {
	private var universities = emptyList<RealmItemUniversity>().toMutableList()
	
	fun getItem(index: Int): RealmItemUniversity? {
		return if (universities.size >= index + 1) universities[index] else null
	}
	
	override fun uploadItems() {
		if (offset == 0) {
			universities = emptyList<RealmItemUniversity>().toMutableList()
			emptyRecyclerDataset()
		}
		Model().getUniversities	{ objects, errorType ->
			if (errorType != null) {
				onErrorCallback(errorType)
			} else {
				universities += objects ?: emptyList()
				val stringsList = emptyList<String>().toMutableList()
				(objects ?: emptyList()).forEach { stringsList.add(it.name ?: "") }
				fillRecyclerDataset(stringsList)
			}
		}
	}
	
	override fun uploadMoreItems() = Unit
}