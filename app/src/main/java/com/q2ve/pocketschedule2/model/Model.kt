package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemMain
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUser
import com.q2ve.pocketschedule2.model.realm.RealmIO
import com.q2ve.pocketschedule2.model.retrofit.RetrofitCalls

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("ПРОВЕРКА НА ДОСТУПНОСТЬ ИНТЕРНЕТА, СУКА")
class Model(private val onError: (ErrorType) -> Unit) {
	
	private fun onRetrofitError(errorType: ErrorType) {
		ErrorResolver().resolveRetrofitError(errorType, onError)
	}
	
	private val retrofitCalls = RetrofitCalls(::onRetrofitError)
	
	fun getUniversities(onSuccess: (List<RealmItemUniversity>) -> Unit) {
		RetrofitCalls(::onRetrofitError).getUniversities { objects ->
			val universities: List<RealmItemUniversity> = objects ?: emptyList()
			RealmIO(onError).insertOrUpdateWithIndexing(universities, onSuccess)
		}
	}
	
	fun getGroups(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	) {
		retrofitCalls.getGroups (offset, limit, university, query)
		{ objects: List<RealmItemScheduleUser>? ->
			RealmIO().insertOrUpdateWithIndexing(objects ?: emptyList(), onSuccess)
		}
	}
	
	fun getProfessors(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	) {
		retrofitCalls.getProfessors (offset, limit, university, query)
		{ objects: List<RealmItemScheduleUser>? ->
			RealmIO().insertOrUpdateWithIndexing(objects ?: emptyList(), onSuccess)
		}
	}

	private fun resolveAuthOnSuccess(
		sessionId: String?,
		user: RealmItemUser?,
		onSuccess: (RealmItemMain) -> Unit
	) {
		when {
			user == null -> onError(ErrorType.UnknownServerError)
			sessionId == null -> onError(ErrorType.UnknownServerError)
			else -> {
				val mainObject = UserObserver.getMainObject()
				mainObject.currentUser = user
				mainObject.sessionId = sessionId
				if (user.scheduleUser != null) {
					mainObject.scheduleUser = user.scheduleUser
				}
				if (user.university != null) {
					mainObject.scheduleUniversity = user.university
				}
				RealmIO(onError).insertOrUpdate(mainObject, onSuccess)
			}
		}
	}
	
	fun postUser(
		login: String,
		password: String,
		service: String,
		onSuccess: (RealmItemMain) -> Unit
	) {
		retrofitCalls.postUser(login, password, service)
		{ sessionId: String?, user: RealmItemUser? ->
			resolveAuthOnSuccess(sessionId, user, onSuccess)
		}
	}
	
	fun postVkUser(
		token: String,
		onSuccess: (RealmItemMain) -> Unit
	) {
		retrofitCalls.postVkUser(token)
		{ sessionId: String?, user: RealmItemUser? ->
			resolveAuthOnSuccess(sessionId, user, onSuccess)
		}
	}
}