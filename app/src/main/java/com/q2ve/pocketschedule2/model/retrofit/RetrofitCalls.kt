package com.q2ve.pocketschedule2.model.retrofit

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversitySHITCRAP
import com.q2ve.pocketschedule2.model.dataclasses.RetrofitItemResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Переименовать!")
class RetrofitCalls {
	/**
	 * Returns ApiRequest configured by RetrofitRequestBuilder.
	 */
	private fun getRequest(): ApiRequests {
		return RetrofitRequestBuilder().getRequest()
	}
	
	/**
	 * Enqueues call with selected callback.
	 */
	private fun <T> enqueueCall(call: Call<T>, callback: Callback<T>) {
		call.enqueue(callback)
	}
	
	/**
	 * Returns ErrorType object if response is unsuccessful.
	 * Returns null if no errors are found.
	 */
	private fun <T> checkItemResponse(response: Response<RetrofitItemResponse<T>>): ErrorType? {
		when {
			(response.body() == null || response.code() == 500) -> {
				return ErrorType.UnknownServerError
			}
			(response.body()!!.result == null) -> {
				val errorType: ErrorType = if (response.body()!!.error != null) {
					when (response.body()!!.error!!.code) {
						"invalid_api_version" -> ErrorType.InvalidApiVersion
						"validation_error" -> ErrorType.ValidationError
						"forbidden" -> ErrorType.OutdatedSession
						"too_often" -> ErrorType.Throttling
						else -> ErrorType.UnknownServerError
					}
				} else ErrorType.UnknownServerError
				return errorType
			}
			(response.body()!!.result!!.totalCount == null) -> return ErrorType.UnknownServerError
			(response.body()!!.result!!.items == null) -> return ErrorType.UnknownServerError
			else -> return null
		}
	}
	
	/**
	 * Returns ErrorType.
	 */
	private fun checkOnFailure(throwable: Throwable): ErrorType {
		return when (throwable.javaClass.name.toString()) {
			"java.net.SocketTimeoutException" -> ErrorType.ConnectionTimeout
			"java.net.UnknownHostException" -> ErrorType.UnknownHost
			else -> ErrorType.UnknownExternalError
		}
	}
	
	//Requests calls
	
	fun getUniversities(externalCallback: (List<RealmItemUniversitySHITCRAP>?, ErrorType?) -> Unit) {
		val callback = object: Callback<RetrofitItemResponse<RealmItemUniversitySHITCRAP>> {
			override fun onResponse(
				call: Call<RetrofitItemResponse<RealmItemUniversitySHITCRAP>>,
				response: Response<RetrofitItemResponse<RealmItemUniversitySHITCRAP>>
			) {
				val errorType = checkItemResponse(response)
				val items = if (errorType == null) (response.body()?.result?.items) else null
				externalCallback(items, errorType)
			}
			override fun onFailure(
				call: Call<RetrofitItemResponse<RealmItemUniversitySHITCRAP>>,
				t: Throwable
			) {
				val errorType = checkOnFailure(t)
				externalCallback(null, errorType)
			}
		}
		val call = getRequest().getUniversities(0, 8)
		enqueueCall(call, callback)
	}
	
	private fun buildScheduleUserCallback(
		externalCallback: (List<RealmItemScheduleUser>?, ErrorType?) -> Unit
	): Callback<RetrofitItemResponse<RealmItemScheduleUser>> {
		return object: Callback<RetrofitItemResponse<RealmItemScheduleUser>> {
			override fun onResponse(
				call: Call<RetrofitItemResponse<RealmItemScheduleUser>>,
				response: Response<RetrofitItemResponse<RealmItemScheduleUser>>
			) {
				val errorType = checkItemResponse(response)
				val items = if (errorType == null) (response.body()?.result?.items) else null
				externalCallback(items, errorType)
			}
			override fun onFailure(
				call: Call<RetrofitItemResponse<RealmItemScheduleUser>>,
				t: Throwable
			) {
				val errorType = checkOnFailure(t)
				externalCallback(null, errorType)
			}
		}
	}
	
	fun getGroups(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		externalCallback: (List<RealmItemScheduleUser>?, ErrorType?) -> Unit
	){
		val callback = buildScheduleUserCallback(externalCallback)
		val call = getRequest().getGroups(university, offset, limit, query)
		enqueueCall(call, callback)
	}
	
	fun getProfessors(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		externalCallback: (List<RealmItemScheduleUser>?, ErrorType?) -> Unit
	){
		val callback = buildScheduleUserCallback(externalCallback)
		val call = getRequest().getProfessors(university, offset, limit, query)
		enqueueCall(call, callback)
	}
}