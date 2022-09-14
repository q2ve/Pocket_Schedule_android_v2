package com.q2ve.schedappv2.ui.recyclerSelector

import com.q2ve.schedappv2.model.ErrorType
import com.q2ve.schedappv2.model.Model
import com.q2ve.schedappv2.model.dataclasses.RealmItemScheduleUser

/**
 * The controller will be unable to amend the recycler`s dataset if
 * the recyclerAdapter will not be provided in the constructor or later.
 */
class RecyclerSelectorUploadingControllerGroups(
	recyclerAdapter: RecyclerSelectorAdapter?,
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
		Model(onErrorCallback).getGroups(offset, offset + paginationStep, universityId, query)
		{ objects ->
			groups += objects
			val stringsList = emptyList<String>().toMutableList()
			objects.forEach { stringsList.add(it.name ?: "-") }
			fillRecyclerDataset(stringsList)
		}
	}
}