package com.q2ve.pocketschedule2.model.retrofit

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversitySHITCRAP
import com.q2ve.pocketschedule2.model.dataclasses.RetrofitItemResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

interface ApiRequests {
	
	//APIv2
	@GET("universities/{university}/groups?")
	fun getGroups (
		@Path("university") university: String,
		@Query("offset") offset: Int,
		@Query("limit") limit: Int,
		@Query("q") q: String = ""
	): Call<RetrofitItemResponse<RealmItemScheduleUser>>
	
	//APIv2
	@GET("universities/{university}/professors?")
	fun getProfessors (
		@Path("university") university: String,
		@Query("offset") offset: Int,
		@Query("limit") limit: Int,
		@Query("q") q: String = ""
	): Call<RetrofitItemResponse<RealmItemScheduleUser>>
	
	//APIv2
	@GET("universities?")
	fun getUniversities (
		@Query("offset") offset: Int,
		@Query("limit") limit: Int,
		@Query("q") q: String = ""
	): Call<RetrofitItemResponse<RealmItemUniversitySHITCRAP>>

//	@GET("universities/{university}/schedule/{scheduleUserId}/?") //APIv2
//	fun getLessons (
//		@Path("university") university: String,
//		@Path("scheduleUserId") scheduleUserId: String
//		//@Query("q") week: String = ""
//	): Call<RetrofitResponseObject<RetrofitItemLesson>>
//
//	@GET("objects/feedSources?")
//	fun getFeedSources (
//		@Header("X-Session-Id") sessionId: String
//	)
//
//	@GET("deadlines") //APIv2
//	fun getDeadlines (
//		@Header("X-Session-Id") sessionId: String
//	): Call<RetrofitResponseObject<RetrofitItemDeadline>>
//
//	@GET("deadlines/sources") //APIv2
//	fun getDeadlineSources (
//		@Header("X-Session-Id") sessionId: String
//	): Call<RetrofitResponseObject<RetrofitItemDeadlineSource>>
//
//	@POST("auth/{service}/") //APIv2
//	fun postUser (
//		@Path("service") service: String,
//		@Body body: RetrofitBodyLoginPassword
//	): Call<RetrofitAuthResponseObject>
//
//	@POST("auth/vk") //APIv2
//	fun postVkUser (
//		@Body body: RetrofitBodyToken
//	): Call<RetrofitAuthResponseObject>
//
//	@POST("deadlines") //APIv2
//	fun postDeadline (
//		@Header("X-Session-Id") sessionId: String,
//		@Body body: RetrofitBodyDeadline
//	): Call<RetrofitDeadlineResponseObject>
//
//	@POST("deadlines/{deadlineId}/close") //APIv2
//	fun postCloseDeadline (
//		@Path("deadlineId") deadlineId: String,
//		@Header("X-Session-Id") sessionId: String
//	): Call<RetrofitDeadlineResponseObject>
//
//	@POST("auth/logout") // APIv2
//	fun postLogout (
//		@Header("X-Session-Id") sessionId: String
//	): Call<Any>
//
//	@PUT("me") //APIv2
//	fun putMe (
//		@Header("X-Session-Id") sessionId: String,
//		@Body body: RetrofitBodyMe
//	): Call<RetrofitMeResponseObject>
//
//	@PUT("me") //APIv2
//	fun putMeServiceLogin (
//		@Header("X-Session-Id") sessionId: String,
//		@Body body: RetrofitBodyLoginPassword
//	): Call<RetrofitMeResponseObject>
//
//	@PUT("deadlines/{deadlineId}") //APIv2
//	fun putDeadline (
//		@Path("deadlineId") deadlineId: String,
//		@Header("X-Session-Id") sessionId: String,
//		@Body body: RetrofitBodyDeadline
//	): Call<RetrofitDeadlineResponseObject>
//
//	@DELETE("deadlines/{deadlineId}/close") //APIv2
//	fun deleteCloseDeadline (
//		@Path("deadlineId") deadlineId: String,
//		@Header("X-Session-Id") sessionId: String
//	): Call<RetrofitDeadlineResponseObject>
//
//	@DELETE("deadlines/{deadlineId}") //APIv2
//	fun deleteDeadline (
//		@Path("deadlineId") deadlineId: String,
//		@Header("X-Session-Id") sessionId: String
//	): Call<RetrofitDeadlineResponseObject>
}