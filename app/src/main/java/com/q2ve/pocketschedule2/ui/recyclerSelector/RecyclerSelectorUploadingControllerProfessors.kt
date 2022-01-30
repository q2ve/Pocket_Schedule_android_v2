package com.q2ve.pocketschedule2.ui.recyclerSelector

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

/**
 * The controller will be unable to amend the recycler`s dataset if
 * the recyclerAdapter will not be provided in the constructor or later.
 */
class RecyclerSelectorUploadingControllerProfessors(
	recyclerAdapter: RecyclerSelectorAdapter?,
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
		Model(onErrorCallback).getProfessors(offset, offset + paginationStep, universityId, query)
		{ objects ->
			professors += objects
			val stringsList = emptyList<String>().toMutableList()
			objects.forEach { stringsList.add(it.name ?: "-") }
			fillRecyclerDataset(stringsList)
		}
	}
}