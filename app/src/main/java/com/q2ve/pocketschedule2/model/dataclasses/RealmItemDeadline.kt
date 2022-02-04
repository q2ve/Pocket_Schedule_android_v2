package com.q2ve.pocketschedule2.model.dataclasses

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemDeadline (
	@PrimaryKey
	var _id: String = "",
	var title: String? = null,
	var description: String? = null,
	var startDate: Int? = null,
	var endDate: Int? = null,
	var isExternal: Boolean? = null,
	var isClosed: Boolean? = null,
	var subject: RealmItemSubject? = null,
	//For tasks below
	var curPoints: Int? = null,
	var type: String? = null,
	var markpoint: Int? = null,
	var reportRequired: Boolean? = null
	//var files: RealmList<File>? = null //Future feature. ToFix - Realm doesn't allow 'File' class.
): RealmObject()