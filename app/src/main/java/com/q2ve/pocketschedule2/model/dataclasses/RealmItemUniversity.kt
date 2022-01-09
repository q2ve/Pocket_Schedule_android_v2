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
	var referenceDate: Int = 1630454400, //TODO("Automatize it. It must be 1.09 of this study year")
	var referenceWeek: String = "odd",
): RealmObject(), RealmNameInterface {
	override fun getObjectName(): String = name
}