package com.q2ve.pocketschedule2.model.dataclasses

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemUniversity (
	@PrimaryKey
	var _id: String? = null,
	var referenceDate: String? = null,
	var referenceWeek: String? = null,
	var name: String? = null,
	var serviceName: String? = null
): RealmObject()