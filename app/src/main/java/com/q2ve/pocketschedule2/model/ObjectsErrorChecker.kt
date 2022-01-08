package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ObjectsErrorChecker {
	private fun checkUniversity(item: RealmItemUniversity?): RealmItemUniversity? {
		return if (item?._id == null || item._id == "" ||
			item.name == null || item.name == "" ||
			item.serviceName == null || item.serviceName == ""
		) null else item
	}
	
	fun checkUniversities(items: List<RealmItemUniversity>): List<RealmItemUniversity> {
		val output = emptyList<RealmItemUniversity>().toMutableList()
		items.forEach { if (checkUniversity(it) != null) output.add(it) }
		return output
	}
}