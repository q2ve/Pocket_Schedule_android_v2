package com.q2ve.schedappv2.model.realm

import com.q2ve.schedappv2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.schedappv2.model.dataclasses.RealmItemUniversity
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.UUID.randomUUID

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class IndexItem (
	@PrimaryKey
	var _id: String = randomUUID().toString(),
	var index: Int = 0,
	var indexedObjectClass: String = "",
	var indexedUniversity: RealmItemUniversity? = null,
	var indexedScheduleUser: RealmItemScheduleUser? = null
): RealmObject()