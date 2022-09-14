package com.q2ve.schedappv2.model.dataclasses

import com.q2ve.schedappv2.model.realm.RealmNameInterface
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemSubject (
	@PrimaryKey
	var _id: String = "",
	var name: String? = null,
	//var deadlines: RealmList<RealmDeadlineObject>? = null //RealmList()
): RealmObject(), RealmNameInterface {
	override fun getObjectName(): String {
		return name ?: "???"
	}
	
}