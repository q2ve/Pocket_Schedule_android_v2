package com.q2ve.schedappv2.model.dataclasses

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemDeadlineSource (
	@PrimaryKey
	var _id: String = "",
	var name: String = "null"
): RealmObject()