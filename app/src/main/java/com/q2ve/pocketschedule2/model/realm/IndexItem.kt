package com.q2ve.pocketschedule2.model.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.UUID.randomUUID

/**
 * Created by Denis Shishkin on 20.06.2021.
 * qwq2eq@gmail.com
 */

open class IndexItem (
	@PrimaryKey
	var _id: String = randomUUID().toString(),
	var index: Int = 0,
	var indexedObject: RealmObject? = null,
	var indexedObjectClass: String = ""
): RealmObject()