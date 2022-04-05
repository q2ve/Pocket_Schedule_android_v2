package com.q2ve.pocketschedule2.model.dataclasses

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemLesson(
	@PrimaryKey
	var _id: String = "",
	var startTime: String? = null,
	var endTime: String? = null,
	var lessonNum: String? = null,
	var day: String? = null,
	var rooms: String? = null,
	var type: String? = null,
	var week: String? = null,
	var tags: String? = null,
	var groups: RealmList<RealmItemScheduleUser>? = null,
	var professors: RealmList<RealmItemScheduleUser>? = null,
	var subject: RealmItemSubject? = null
): RealmObject()