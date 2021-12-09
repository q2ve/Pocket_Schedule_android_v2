package com.q2ve.pocketschedule2.model.retrofit

import com.q2ve.pocketschedule2.helpers.Constants
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RetrofitRequestBuilder {
	fun getRequest(): ApiRequests {
		val httpBuilder = OkHttpClient.Builder()
		httpBuilder.callTimeout(20, TimeUnit.SECONDS)
		httpBuilder.connectTimeout(20, TimeUnit.SECONDS)
		httpBuilder.readTimeout(20, TimeUnit.SECONDS)
		httpBuilder.writeTimeout(20, TimeUnit.SECONDS)
		val certificatePinner = CertificatePinner.Builder()
			.add(Constants.serverUrl, Constants.serverCertFingerprint)
			.build()
		val client = httpBuilder.certificatePinner(certificatePinner).build()
		return Retrofit.Builder()
			.client(client)
			.baseUrl(Constants.serverUrlApi)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ApiRequests::class.java)
	}
}