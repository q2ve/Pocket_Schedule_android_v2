package com.q2ve.pocketschedule2.model.retrofit

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
		val callback = object: Callback<RetrofitItemResponse<RealmItemUniversity>> {
			override fun onResponse(
				call: Call<RetrofitItemResponse<RealmItemUniversity>>,
				response: Response<RetrofitItemResponse<RealmItemUniversity>>
			) {
				val errorType = RetrofitErrorResolver().checkItemResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitItemResponse<RealmItemUniversity>>,
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
	): Callback<RetrofitItemResponse<RealmItemScheduleUser>> {
		return object: Callback<RetrofitItemResponse<RealmItemScheduleUser>> {
			override fun onResponse(
				call: Call<RetrofitItemResponse<RealmItemScheduleUser>>,
				response: Response<RetrofitItemResponse<RealmItemScheduleUser>>
			) {
				val errorType = RetrofitErrorResolver().checkItemResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.items)
			}
			override fun onFailure(
				call: Call<RetrofitItemResponse<RealmItemScheduleUser>>,
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
	): Callback<RetrofitItemAuthResponse>  {
		return object: Callback<RetrofitItemAuthResponse> {
			override fun onResponse(
				call: Call<RetrofitItemAuthResponse>,
				response: Response<RetrofitItemAuthResponse>
			) {
				val errorType = RetrofitErrorResolver().checkItemAuthResponse(response)
				if (errorType != null) onError(errorType)
				else onSuccess(response.body()?.result?.sessionId, response.body()?.result?.user)
			}
			override fun onFailure(
				call: Call<RetrofitItemAuthResponse>,
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
}