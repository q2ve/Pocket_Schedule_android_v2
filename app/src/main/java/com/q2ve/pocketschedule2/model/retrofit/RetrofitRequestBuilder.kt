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
		val timeout = Constants.connectionTimeout
		httpBuilder.callTimeout(timeout, TimeUnit.SECONDS)
		httpBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
		httpBuilder.readTimeout(timeout, TimeUnit.SECONDS)
		httpBuilder.writeTimeout(timeout, TimeUnit.SECONDS)
		val client: OkHttpClient =
			if (Constants.enableCertificatePining) {
				val certificatePinner = CertificatePinner.Builder()
					.add(Constants.serverUrl, Constants.serverCertFingerprint)
					.build()
				httpBuilder.certificatePinner(certificatePinner).build()
			} else {
				httpBuilder.build()
			}
		return Retrofit.Builder()
			.client(client)
			.baseUrl(Constants.serverUrlApi)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(ApiRequests::class.java)
	}
}