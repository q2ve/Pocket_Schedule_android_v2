package com.q2ve.pocketschedule2.helpers.recyclerSelector

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversitySHITCRAP

class RecyclerSelectorUploadingControllerUniversities(
	recyclerAdapter: RecyclerSelectorAdapter,
	private val onErrorCallback: (ErrorType) -> Unit
): RecyclerSelectorUploadingControllerBase(recyclerAdapter) {
	private var universities = emptyList<RealmItemUniversitySHITCRAP>().toMutableList()
	
	fun getItem(index: Int): RealmItemUniversitySHITCRAP? {
		return if (universities.size >= index + 1) universities[index] else null
	}
	
	override fun uploadItems() {
		if (offset == 0) {
			universities = emptyList<RealmItemUniversitySHITCRAP>().toMutableList()
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