package com.q2ve.pocketschedule2.ui.recyclerSelector

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemSubject

/**
 * The controller will be unable to amend the recycler`s dataset if
 * the recyclerAdapter will not be provided in the constructor or later.
 */
class RecyclerSelectorUploadingControllerSubjects(
	recyclerAdapter: RecyclerSelectorAdapter?,
	private val onErrorCallback: (ErrorType) -> Unit,
	private val universityId: String,
	private val scheduleUserId: String
): RecyclerSelectorUploadingControllerBase(recyclerAdapter) {
	private var subjects = emptyList<RealmItemSubject?>().toMutableList()
	private var subjectsForSearch = emptyList<RealmItemSubject?>().toMutableList()
	
	fun getItem(index: Int): RealmItemSubject? {
		return if (subjects.size >= index + 1) subjects[index] else null
	}
	
	override fun uploadItems() {
		subjects = emptyList<RealmItemSubject>().toMutableList()
		emptyRecyclerDataset()
		if (query.isEmpty()) {
			Model(onErrorCallback).getLessons(universityId, scheduleUserId)
			{ lessons ->
				val stringsList = emptyList<String>().toMutableList()
				subjects += null
				stringsList += "----"
				lessons.forEach {
					it.subject?.let { subject ->
						subject.name?.let { name ->
							if (!stringsList.contains(name)) {
								subjects.add(subject)
								stringsList.add(name)
							}
						}
					}
				}
				subjectsForSearch = subjects
				fillRecyclerDataset(stringsList)
			}
		} else {
			val filteredSubjects = emptyList<RealmItemSubject?>().toMutableList()
			val stringsList = emptyList<String>().toMutableList()
			filteredSubjects += null
			stringsList += "----"
			subjectsForSearch.forEach {
				it?.let { subject ->
					subject.name?.let { name ->
						if (name.contains(query)) {
							filteredSubjects += subject
							stringsList += name
						}
					}
				}
			}
			subjects = filteredSubjects
			fillRecyclerDataset(stringsList)
		}
	}
	
	override fun uploadMoreItems() = Unit
}