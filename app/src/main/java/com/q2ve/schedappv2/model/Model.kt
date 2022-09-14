package com.q2ve.schedappv2.model

import com.q2ve.schedappv2.helpers.UserObserver
import com.q2ve.schedappv2.model.dataclasses.*
import com.q2ve.schedappv2.model.realm.RealmIO
import com.q2ve.schedappv2.model.retrofit.RetrofitCalls

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
	
	fun getDeadlineSources(sessionId: String, onSuccess: (List<RealmItemDeadlineSource>) -> Unit) {
		retrofitCalls.getDeadlineSources(sessionId) { objects ->
			val checkedObjects = checker.checkList(
				objects ?: emptyList(),
				checker::checkDeadlineSource
			)
			realm.insertOrUpdate(checkedObjects, onSuccess)
		}
	}
	
	private fun onDeadlinesGetSuccess(
		deadlines: List<RealmItemDeadline>?,
		onSuccess: (List<RealmItemDeadline>) -> Unit
	) {
		//TODO("Для работы оффлайн надо будет реализовать сохранение индекс-списков. Тогда можно будет легко получать список нужных дедлайнов.")
		val checkedObjects = checker.checkList(
			deadlines ?: emptyList(),
			checker::checkDeadline
		)
		realm.insertOrUpdate(deadlines ?: emptyList(), onSuccess)
	}
	
	fun getOpenedDeadlines(sessionId: String, onSuccess: (List<RealmItemDeadline>) -> Unit) {
		retrofitCalls.getOpenedDeadlines(sessionId) {
			realm.deleteDeadlines(false) { onDeadlinesGetSuccess(it, onSuccess) }
		}
	}
	
	fun getClosedDeadlines(sessionId: String, onSuccess: (List<RealmItemDeadline>) -> Unit) {
		retrofitCalls.getClosedDeadlines(sessionId) {
			realm.deleteDeadlines(true) { onDeadlinesGetSuccess(it, onSuccess) }
		}
	}
	
	fun getExpiredDeadlines(
		sessionId: String,
		currentTime: Int,
		onSuccess: (List<RealmItemDeadline>) -> Unit
	) {
		retrofitCalls.getExpiredDeadlines(sessionId, currentTime) {
			realm.deleteDeadlines(false, currentTime) { onDeadlinesGetSuccess(it, onSuccess) }
		}
	}
	
	fun getExternalDeadlines(
		sessionId: String,
		deadlineSourceId: String,
		onSuccess: (List<RealmItemDeadline>) -> Unit
	) {
		retrofitCalls.getExternalDeadlines(sessionId, deadlineSourceId)
		{ onDeadlinesGetSuccess(it, onSuccess) }
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
	
	private fun resolveDeadlineOnSuccess(
		deadline: RealmItemDeadline?,
		onSuccess: (RealmItemDeadline) -> Unit
	) {
		checker.checkDeadline(deadline)?.let { realm.insertOrUpdate(it, onSuccess) }
	}
	
	fun putDeadline(
		sessionId: String,
		deadline: RealmItemDeadline,
		onSuccess: (RealmItemDeadline) -> Unit
	) {
		retrofitCalls.putDeadline(
			sessionId,
			deadline
		) { resolveDeadlineOnSuccess(it, onSuccess) }
	}
	
	fun createDeadline(
		sessionId: String,
		deadline: RealmItemDeadline,
		onSuccess: (RealmItemDeadline) -> Unit
	) {
		retrofitCalls.postDeadline(
			sessionId,
			deadline
		) { resolveDeadlineOnSuccess(it, onSuccess) }
	}
	
	fun openDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline) -> Unit
	) {
		retrofitCalls.deleteDeadlineCloseEndpoint(
			sessionId,
			deadlineId
		) { resolveDeadlineOnSuccess(it, onSuccess) }
	}
	
	fun closeDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline) -> Unit
	) {
		retrofitCalls.postDeadlineCloseEndpoint(
			sessionId,
			deadlineId
		) { resolveDeadlineOnSuccess(it, onSuccess) }
	}
	
	fun deleteDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: () -> Unit
	) {
		retrofitCalls.deleteDeadline(sessionId, deadlineId) {
			if (it != null) realm.delete(it, onSuccess)
		}
	}
	
	fun updateMainObject(mainObject: RealmItemMain, onSuccess: (RealmItemMain) -> Unit) {
		realm.insertOrUpdate(mainObject, onSuccess)
	}
	
	fun getOpenedDeadlinesRealmResults() =
		realm.getDeadlinesRealmResultsSet(false)
	
	fun getClosedDeadlinesRealmResults() =
		realm.getDeadlinesRealmResultsSet(true)
	
	fun getExpiredDeadlinesRealmResults(time: Int) =
		realm.getDeadlinesRealmResultsSet(false, time)
}