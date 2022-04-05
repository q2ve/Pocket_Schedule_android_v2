package com.q2ve.pocketschedule2.model.dataclasses

import com.q2ve.pocketschedule2.helpers.Constants
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemMain (
	@PrimaryKey
	var _id: String = Constants.mainObjectId,
	var currentUser: RealmItemUser? = null,
	var sessionId: String?= null,
	var scheduleUser: RealmItemScheduleUser? = null,
	var scheduleUniversity: RealmItemUniversity? = null,
	var startScreen: Int = 1
): RealmObject()