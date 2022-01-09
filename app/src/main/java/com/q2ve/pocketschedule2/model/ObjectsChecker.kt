package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUser

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ObjectsChecker {
	fun <T> checkList(items: List<T>, checkItemMethod: (T?) -> T?): List<T> {
		var isIncorrectItemPresented = false
		val output = emptyList<T>().toMutableList()
		items.forEach {
			if (checkItemMethod(it) != null) output.add(it)
			else isIncorrectItemPresented = true
		}
		if (isIncorrectItemPresented) ErrorDeclarer().declareIncorrectObjectError()
		return output
	}
	
	fun checkUniversity(item: RealmItemUniversity?): RealmItemUniversity? {
		return when {
			(item?._id == null || item._id == "" ||
			 item.name == null || item.name == "" ||
			 item.serviceName == null || item.serviceName == "") -> null
			
			(!(item.referenceWeek == "odd" || item.referenceWeek == "even")) -> {
				item.referenceWeek = "odd"
				item
			}
			
			(item.referenceDate == null) -> {
				item.referenceDate = 1630454400 //TODO("Automatize it. It must be 1.09 of this study year")
				item
			}
			
			else -> item
		}
	}
	
	fun checkScheduleUser(item: RealmItemScheduleUser?): RealmItemScheduleUser? {
		return when {
			(item?._id == null || item._id == "" || item.name == null || item.name == "") -> null
			else -> item
		}
	}
	
	fun checkUser(item: RealmItemUser?): RealmItemUser? {
		return if (item?._id == null || item._id == "") null else item
	}
}