package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.model.dataclasses.*
import com.q2ve.pocketschedule2.model.realm.RealmIO
import com.q2ve.pocketschedule2.model.retrofit.RetrofitCalls

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("ПРОВЕРКА НА ДОСТУПНОСТЬ ИНТЕРНЕТА, СУКА")
class Model(private val onError: (ErrorType) -> Unit) {
	
	private fun onRetrofitError(errorType: ErrorType) {
		ErrorDeclarer().declareRetrofitError(errorType, onError)
	}
	
	private val retrofitCalls = RetrofitCalls(::onRetrofitError)
	private val realm = RealmIO(onError)
	private val checker = ObjectsChecker()
	
	fun getUniversities(onSuccess: (List<RealmItemUniversity>) -> Unit) {
		retrofitCalls.getUniversities { objects ->
			val checkedObjects = checker.checkList(
				objects ?: emptyList(),
				checker::checkUniversity
			)
			realm.insertOrUpdateWithIndexing(checkedObjects, onSuccess)
		}
	}
	
	private fun resolveScheduleUserOnSuccess(
		objects: List<RealmItemScheduleUser>?,
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	) {
		val checkedObjects = checker.checkList(
			objects ?: emptyList(),
			checker::checkScheduleUser
		)
		realm.insertOrUpdateWithIndexing(checkedObjects, onSuccess)
	}
	
	fun getGroups(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	) {
		retrofitCalls.getGroups (offset, limit, university, query)
		{ resolveScheduleUserOnSuccess(it, onSuccess) }
	}
	
	fun getProfessors(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>) -> Unit
	) {
		retrofitCalls.getProfessors (offset, limit, university, query)
		{ resolveScheduleUserOnSuccess(it, onSuccess) }
	}
	
	fun getLessons(
		university: String,
		scheduleUser: String,
		onSuccess: (List<RealmItemLesson>) -> Unit
	) {
		retrofitCalls.getLessons(university, scheduleUser) { objects ->
			val checkedObjects = checker.checkList(
				objects ?: emptyList(),
				checker::checkLesson
			)
			realm.insertOrUpdate(checkedObjects, onSuccess)
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
			(checker.checkUser(user) == null) -> onError(ErrorType.IncorrectObject)
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
				realm.insertOrUpdate(mainObject, onSuccess)
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
	
	fun postLogout(sessionId: String, onSuccess: (() -> Unit)? = null) {
		retrofitCalls.postLogout(sessionId) { onSuccess?.let { it() } }
	}
	
	fun putMe(
		sessionId: String,
		universityId: String,
		scheduleUserId: String,
		onSuccess: (RealmItemUser) -> Unit
	) {
		retrofitCalls.putMe(
			sessionId,
			universityId,
			scheduleUserId
		) { user: RealmItemUser? -> resolveAuthOnSuccess(sessionId, user) { onSuccess(user!!) } }
	}
	
	fun putMeServiceLogin(
		sessionId: String,
		serviceLogin: String,
		servicePassword: String,
		onSuccess: (RealmItemUser) -> Unit
	) {
		retrofitCalls.putMeServiceLogin(
			sessionId,
			serviceLogin,
			servicePassword
		) { user: RealmItemUser? -> resolveAuthOnSuccess(sessionId, user) { onSuccess(user!!) } }
	}
	
	fun updateMainObject(mainObject: RealmItemMain, onSuccess: (RealmItemMain) -> Unit) {
		realm.insertOrUpdate(mainObject, onSuccess)
	}
}