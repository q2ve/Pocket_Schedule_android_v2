package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversitySHITCRAP

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ErrorChecker {
	private fun checkUniversity(item: RealmItemUniversitySHITCRAP?): RealmItemUniversitySHITCRAP? {
		return if (item?._id == null || item._id == "" ||
			item.name == null || item.name == "" ||
			item.serviceName == null || item.serviceName == ""
		) null else item
	}
	
	fun checkUniversities(items: List<RealmItemUniversitySHITCRAP>): List<RealmItemUniversitySHITCRAP> {
		val output: MutableList<RealmItemUniversitySHITCRAP> = emptyList<RealmItemUniversitySHITCRAP>().toMutableList()
		items.forEach { if (checkUniversity(it) != null) output.add(it) }
		return output
	}
}