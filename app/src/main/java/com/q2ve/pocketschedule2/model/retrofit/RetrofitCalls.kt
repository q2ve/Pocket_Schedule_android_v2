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
	
	fun putMe(
		sessionId: String,
		universityId: String,
		scheduleUserId: String,
		onSuccess: (RealmItemUser?) -> Unit
	) {
		val callback = object: Callback<RetrofitResponseMe> {
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
		val body = RetrofitBodyMe(scheduleUserId, universityId)
		val call = RetrofitCaller().getRequest().putMe(sessionId, body)
		RetrofitCaller().enqueueCall(call, callback)
	}
}