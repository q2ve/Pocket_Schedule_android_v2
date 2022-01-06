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
	@JvmField
	var _id: String = "",
	var referenceDate: String? = null,
	var referenceWeek: String? = null,
	var name: String? = null,
	var serviceName: String? = null
): RealmObject(), RealmNameInterface {
	fun set_id(value: String?) { _id = value ?: "" }
	fun get_id(): String = _id
	override fun getObjectName(): String = name ?: ""
}