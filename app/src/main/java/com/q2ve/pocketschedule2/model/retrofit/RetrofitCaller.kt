package com.q2ve.pocketschedule2.model.retrofit

import retrofit2.Call
import retrofit2.Callback

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RetrofitCaller {
	/**
	 * Returns ApiRequest configured by RetrofitRequestBuilder.
	 */
	fun getRequest(): ApiRequests {
		return RetrofitRequestBuilder().getRequest()
	}
	
	/**
	 * Enqueues call with selected callback.
	 */
	fun <T> enqueueCall(call: Call<T>, callback: Callback<T>) {
		call.enqueue(callback)
	}
}