package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.*
import com.q2ve.pocketschedule2.ui.core.deadlines.DeadlinesRealmResultsSet

interface ModelInterface {
	fun getUniversities(onSuccess: (List<RealmItemUniversity>) -> Unit)
	
	fun getGroups(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	)
	fun getProfessors(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	)
	fun getLessons(
		university: String,
		scheduleUser: String,
		onSuccess: (List<RealmItemLesson>) -> Unit
	)
	fun getDeadlineSources(sessionId: String, onSuccess: (List<RealmItemDeadlineSource>) -> Unit)
	
	fun getOpenedDeadlines(sessionId: String, onSuccess: (List<RealmItemDeadline>) -> Unit)
	
	fun getClosedDeadlines(sessionId: String, onSuccess: (List<RealmItemDeadline>) -> Unit)
	
	fun getExpiredDeadlines(
		sessionId: String,
		currentTime: Int,
		onSuccess: (List<RealmItemDeadline>) -> Unit
	)
	fun getExternalDeadlines(
		sessionId: String,
		deadlineSourceId: String,
		onSuccess: (List<RealmItemDeadline>) -> Unit
	)
	fun postUser(
		login: String,
		password: String,
		service: String,
		onSuccess: (RealmItemMain) -> Unit
	)
	fun postVkUser(
		token: String,
		onSuccess: (RealmItemMain) -> Unit
	)
	fun postLogout(sessionId: String, onSuccess: (() -> Unit)? = null)
	
	fun putMe(
		sessionId: String,
		universityId: String,
		scheduleUserId: String,
		onSuccess: (RealmItemUser) -> Unit
	)
	fun putMeServiceLogin(
		sessionId: String,
		serviceLogin: String,
		servicePassword: String,
		onSuccess: (RealmItemUser) -> Unit
	)
	fun putDeadline(
		sessionId: String,
		deadline: RealmItemDeadline,
		onSuccess: (RealmItemDeadline) -> Unit
	)
	fun createDeadline(
		sessionId: String,
		deadline: RealmItemDeadline,
		onSuccess: (RealmItemDeadline) -> Unit
	)
	fun openDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline) -> Unit
	)
	fun closeDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline) -> Unit
	)
	fun deleteDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: () -> Unit
	)
	fun updateMainObject(mainObject: RealmItemMain, onSuccess: (RealmItemMain) -> Unit)
	
	fun getOpenedDeadlinesRealmResults(): DeadlinesRealmResultsSet
	
	fun getClosedDeadlinesRealmResults(): DeadlinesRealmResultsSet
	
	fun getExpiredDeadlinesRealmResults(time: Int): DeadlinesRealmResultsSet
}