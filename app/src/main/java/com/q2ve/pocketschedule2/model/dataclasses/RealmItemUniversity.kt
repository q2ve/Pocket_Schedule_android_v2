package com.q2ve.pocketschedule2.model.dataclasses

import com.q2ve.pocketschedule2.model.realm.RealmNameInterface
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemUniversity (
	@PrimaryKey
	var _id: String = "",
	var name: String = "",
	var serviceName: String = "",
	var referenceDate: Int = 1630454400, //TODO("Automatize it (in function below). It must be 1.09 of this study year by default.")
	var referenceWeek: String = "odd",
): RealmObject(), RealmNameInterface {
	override fun getObjectName(): String = name
	
	fun getUniversityReferenceDate(): Int {
//		val date: Date
//		val month = date.month + 1
//		val year = if (month in 1..8) date.year - 1 else date.year
//		val referenceDate = SimpleDateFormat("dd.MM.yyyy").parse("01.09.$year")
		return referenceDate
	}
}