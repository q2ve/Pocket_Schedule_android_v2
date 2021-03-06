package com.q2ve.pocketschedule2.model.retrofit

import android.util.Log
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RetrofitCalls(private val onError: (ErrorType) -> Unit) {
	
	fun getUniversities(
		onSuccess: (List<RealmItemUniversity>?) -> Unit
	) {
		val callback = object: Callback<RetrofitResponse<RealmItemUniversity>> {
			override fun onResponse(
				call: Call<RetrofitResponse<RealmItemUniversity>>,
				response: Response<RetrofitResponse<RealmItemUniversity>>
			) {
				val errorType = RetrofitErrorResolver().checkResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitResponse<RealmItemUniversity>>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
		val call = RetrofitCaller().getRequest().getUniversities(0, 8)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	private fun buildScheduleUserCallback(
		onSuccess: (List<RealmItemScheduleUser>?) -> Unit
	): Callback<RetrofitResponse<RealmItemScheduleUser>> {
		return object: Callback<RetrofitResponse<RealmItemScheduleUser>> {
			override fun onResponse(
				call: Call<RetrofitResponse<RealmItemScheduleUser>>,
				response: Response<RetrofitResponse<RealmItemScheduleUser>>
			) {
				val errorType = RetrofitErrorResolver().checkResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitResponse<RealmItemScheduleUser>>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
	}
	
	fun getGroups(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>?) -> Unit
	){
		val callback = buildScheduleUserCallback(onSuccess)
		val call = RetrofitCaller().getRequest().getGroups(university, offset, limit, query)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun getProfessors(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		onSuccess: (List<RealmItemScheduleUser>?) -> Unit
	){
		val callback = buildScheduleUserCallback(onSuccess)
		val call = RetrofitCaller().getRequest().getProfessors(university, offset, limit, query)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun getLessons(
		university: String,
		scheduleUser: String,
		onSuccess: (List<RealmItemLesson>?) -> Unit
	) {
		val callback = object: Callback<RetrofitResponse<RealmItemLesson>> {
			override fun onResponse(
				call: Call<RetrofitResponse<RealmItemLesson>>,
				response: Response<RetrofitResponse<RealmItemLesson>>
			) {
				val errorType = RetrofitErrorResolver().checkResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitResponse<RealmItemLesson>>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
		val call = RetrofitCaller().getRequest().getLessons(university, scheduleUser)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun getDeadlineSources(sessionId: String, onSuccess: (List<RealmItemDeadlineSource>?) -> Unit) {
		val callback = object: Callback<RetrofitResponse<RealmItemDeadlineSource>> {
			override fun onResponse(
				call: Call<RetrofitResponse<RealmItemDeadlineSource>>,
				response: Response<RetrofitResponse<RealmItemDeadlineSource>>
			) {
				val errorType = RetrofitErrorResolver().checkResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitResponse<RealmItemDeadlineSource>>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
		val call = RetrofitCaller().getRequest().getDeadlineSources(sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	private fun buildDeadlinesCallback(
		onSuccess: (List<RealmItemDeadline>?) -> Unit
	): Callback<RetrofitResponse<RealmItemDeadline>>{
		return object: Callback<RetrofitResponse<RealmItemDeadline>> {
			override fun onResponse(
				call: Call<RetrofitResponse<RealmItemDeadline>>,
				response: Response<RetrofitResponse<RealmItemDeadline>>
			) {
				val errorType = RetrofitErrorResolver().checkResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitResponse<RealmItemDeadline>>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
	}
	
	fun getOpenedDeadlines(sessionId: String, onSuccess: (List<RealmItemDeadline>?) -> Unit) {
		val callback = buildDeadlinesCallback(onSuccess)
		val call = RetrofitCaller().getRequest().getOpenedDeadlines(sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun getClosedDeadlines(sessionId: String, onSuccess: (List<RealmItemDeadline>?) -> Unit) {
		val callback = buildDeadlinesCallback(onSuccess)
		val call = RetrofitCaller().getRequest().getClosedDeadlines(sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun getExpiredDeadlines(
		sessionId: String,
		currentTime: Int,
		onSuccess: (List<RealmItemDeadline>?) -> Unit
	) {
		val callback = buildDeadlinesCallback(onSuccess)
		val call = RetrofitCaller().getRequest().getExpiredDeadlines(sessionId, currentTime)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun getExternalDeadlines(
		sessionId: String,
		deadlineSourceId: String,
		onSuccess: (List<RealmItemDeadline>?) -> Unit
	) {
		val callback = buildDeadlinesCallback(onSuccess)
		val call = RetrofitCaller().getRequest().getExternalDeadlines(sessionId, deadlineSourceId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	private fun buildAuthCallback(
		onSuccess: (String?, RealmItemUser?) -> Unit
	): Callback<RetrofitResponseAuth>  {
		return object: Callback<RetrofitResponseAuth> {
			override fun onResponse(
				call: Call<RetrofitResponseAuth>,
				response: Response<RetrofitResponseAuth>
			) {
				val errorType = RetrofitErrorResolver().checkResponseAuth(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.sessionId, response.body()?.result?.user)
			}
			override fun onFailure(
				call: Call<RetrofitResponseAuth>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
	}
	
	fun postVkUser(
		token: String,
		onSuccess: (String?, RealmItemUser?) -> Unit
	) {
		val callback = buildAuthCallback(onSuccess)
		val body = RetrofitBodyToken(token)
		val call = RetrofitCaller().getRequest().postVkUser(body)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun postUser(
		login: String,
		password: String,
		service: String,
		onSuccess: (String?, RealmItemUser?) -> Unit
	) {
		val callback = buildAuthCallback(onSuccess)
		val body = RetrofitBodyLoginPassword(login, password)
		val call = RetrofitCaller().getRequest().postUser(service, body)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun postLogout(sessionId: String, onSuccess: () -> Unit) {
		val callback = object: Callback<Any> {
			override fun onResponse(call: Call<Any>, response: Response<Any>) { onSuccess() }
			override fun onFailure(call: Call<Any>, t: Throwable) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
		val call = RetrofitCaller().getRequest().postLogout(sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	private fun buildDeadlineCallback(
		onSuccess: (RealmItemDeadline?) -> Unit
	): Callback<RetrofitResponseDeadline>{
		return object: Callback<RetrofitResponseDeadline> {
			override fun onResponse(
				call: Call<RetrofitResponseDeadline>,
				response: Response<RetrofitResponseDeadline>
			) {
				val errorType = RetrofitErrorResolver().checkResponseDeadline(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result)
			}
			override fun onFailure(
				call: Call<RetrofitResponseDeadline>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
	}
	
	fun postDeadline(
		sessionId: String,
		deadline: RealmItemDeadline,
		onSuccess: (RealmItemDeadline?) -> Unit
	) {
		val body = RetrofitBodyDeadline(
			deadline.title,
			deadline.description,
			deadline.endDate,
			deadline.subject?._id
		)
		val callback = buildDeadlineCallback(onSuccess)
		val call = RetrofitCaller().getRequest().postDeadline(sessionId, body)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun postDeadlineCloseEndpoint(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline?) -> Unit
	) {
		val callback = buildDeadlineCallback(onSuccess)
		val call = RetrofitCaller().getRequest().postDeadlineCloseEndpoint(deadlineId, sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	private fun buildPutMeCallback(
		onSuccess: (RealmItemUser?) -> Unit
	): Callback<RetrofitResponseMe> {
		return object: Callback<RetrofitResponseMe> {
			override fun onResponse(
				call: Call<RetrofitResponseMe>,
				response: Response<RetrofitResponseMe>
			) {
				val errorType = RetrofitErrorResolver().checkResponseMe(response)
				if (errorType != null) onError(errorType) else onSuccess(response.body()?.result)
				Log.e("response", response.toString())
				Log.e("response.body", response.body().toString())
			}
			override fun onFailure(
				call: Call<RetrofitResponseMe>,
				t: Throwable
			) {
				val errorType = RetrofitErrorResolver().checkOnFailure(t)
				onError(errorType)
			}
		}
	}
	
	fun putMe(
		sessionId: String,
		universityId: String,
		scheduleUserId: String,
		onSuccess: (RealmItemUser?) -> Unit
	) {
		val callback = buildPutMeCallback(onSuccess)
		val body = RetrofitBodyMe(scheduleUserId, universityId)
		val call = RetrofitCaller().getRequest().putMe(sessionId, body)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun putMeServiceLogin(
		sessionId: String,
		serviceLogin: String,
		servicePassword: String,
		onSuccess: (RealmItemUser?) -> Unit
	) {
		val callback = buildPutMeCallback(onSuccess)
		val body = RetrofitBodyLoginPassword(serviceLogin, servicePassword)
		val call = RetrofitCaller().getRequest().putMeServiceLogin(sessionId, body)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun putDeadline(
		sessionId: String,
		deadline: RealmItemDeadline,
		onSuccess: (RealmItemDeadline?) -> Unit
	) {
		val body = RetrofitBodyDeadline(
			deadline.title,
			deadline.description,
			deadline.endDate,
			deadline.subject?._id
		)
		val callback = buildDeadlineCallback(onSuccess)
		val call = RetrofitCaller().getRequest().putDeadline(deadline._id, sessionId, body)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun deleteDeadlineCloseEndpoint(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline?) -> Unit
	) {
		val callback = buildDeadlineCallback(onSuccess)
		val call = RetrofitCaller().getRequest().deleteDeadlineCloseEndpoint(deadlineId, sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
	
	fun deleteDeadline(
		sessionId: String,
		deadlineId: String,
		onSuccess: (RealmItemDeadline?) -> Unit
	) {
		val callback = buildDeadlineCallback(onSuccess)
		val call = RetrofitCaller().getRequest().deleteDeadline(deadlineId, sessionId)
		RetrofitCaller().enqueueCall(call, callback)
	}
}