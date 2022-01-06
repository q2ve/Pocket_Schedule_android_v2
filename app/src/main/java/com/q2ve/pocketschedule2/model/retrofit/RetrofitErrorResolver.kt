package com.q2ve.pocketschedule2.model.retrofit

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.RetrofitItemAuthResponse
import com.q2ve.pocketschedule2.model.dataclasses.RetrofitItemError
import com.q2ve.pocketschedule2.model.dataclasses.RetrofitItemResponse
import retrofit2.Response

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RetrofitErrorResolver {
	
	/**
	 * Returns ErrorType.
	 */
	private fun checkItemError(error: RetrofitItemError?): ErrorType {
		return if (error != null) {
			when (error.code) {
				"invalid_api_version" -> ErrorType.InvalidApiVersion
				"validation_error" -> ErrorType.ValidationError
				"forbidden" -> ErrorType.OutdatedSession
				"too_often" -> ErrorType.Throttling
				else -> ErrorType.UnknownServerError
			}
		} else ErrorType.UnknownServerError
	}
	
	/**
	 * Returns ErrorType object if response is unsuccessful.
	 * Returns null if no errors are found.
	 */
	fun <T> checkItemResponse(response: Response<RetrofitItemResponse<T>>): ErrorType? {
		return when {
			(response.body() == null || response.code() == 500) -> {
				ErrorType.UnknownServerError
			}
			(response.body()!!.result == null) -> {
				checkItemError(response.body()!!.error)
			}
			(response.body()!!.result!!.totalCount == null) -> ErrorType.UnknownServerError
			(response.body()!!.result!!.items == null) -> ErrorType.UnknownServerError
			else -> null
		}
	}
	
	/**
	 * Returns ErrorType object if response is unsuccessful.
	 * Returns null if no errors are found.
	 */
	fun checkItemAuthResponse(response: Response<RetrofitItemAuthResponse>): ErrorType? {
		return when {
			(response.body() == null || response.code() == 500) -> {
				ErrorType.UnknownServerError
			}
			(response.body()!!.result == null) -> {
				checkItemError(response.body()!!.error)
			}
			(response.body()!!.result!!.sessionId == null) -> ErrorType.UnknownServerError
			(response.body()!!.result!!.user == null) -> ErrorType.UnknownServerError
			else -> null
		}
	}
	
	/**
	 * Returns ErrorType.
	 */
	fun checkOnFailure(throwable: Throwable): ErrorType {
		return when (throwable.javaClass.name.toString()) {
			"java.net.SocketTimeoutException" -> ErrorType.ConnectionTimeout
			"java.net.UnknownHostException" -> ErrorType.UnknownHost
			else -> ErrorType.UnknownExternalError
		}
	}
}