package com.q2ve.pocketschedule2.model.dataclasses

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

open class RealmItemUser(
	@PrimaryKey
	var _id: String = "",
	var firstName: String? = null,
	var lastName: String? = null,
	var serviceLogin: String? = null,
	var avatarURL: String? = null,
	var vkId: Int? = null,
	var university: RealmItemUniversity? = null,
	var scheduleUser: RealmItemScheduleUser? = null
): RealmObject()