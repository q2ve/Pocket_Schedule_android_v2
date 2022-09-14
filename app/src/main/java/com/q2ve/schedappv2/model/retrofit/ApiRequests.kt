package com.q2ve.schedappv2.model.retrofit

import com.q2ve.schedappv2.model.dataclasses.*
import retrofit2.Call
import retrofit2.http.*

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
		@Query("q") q: String = "",
		@Query("sort") sort: String = "name"
	): Call<RetrofitResponse<RealmItemScheduleUser>>
	
	//APIv2
	@GET("universities/{university}/professors?")
	fun getProfessors (
		@Path("university") university: String,
		@Query("offset") offset: Int,
		@Query("limit") limit: Int,
		@Query("q") q: String = "",
		@Query("sort") sort: String = "name"
	): Call<RetrofitResponse<RealmItemScheduleUser>>
	
	//APIv2
	@GET("universities?")
	fun getUniversities (
		@Query("offset") offset: Int,
		@Query("limit") limit: Int,
		@Query("q") q: String = ""
	): Call<RetrofitResponse<RealmItemUniversity>>
	
	//APIv2
	@GET("universities/{university}/schedule/{scheduleUserId}/?")
	fun getLessons (
		@Path("university") university: String,
		@Path("scheduleUserId") scheduleUserId: String
	): Call<RetrofitResponse<RealmItemLesson>>

//	@GET("objects/feedSources?")
//	fun getFeedSources (
//		@Header("X-Session-Id") sessionId: String
//	)
	
	//APIv2
	@GET("deadlines")
	fun getOpenedDeadlines (
		@Header("X-Session-Id") sessionId: String,
		@Query("search[isClosed]") search: Boolean = false
	): Call<RetrofitResponse<RealmItemDeadline>>
	
	//APIv2
	@GET("deadlines")
	fun getClosedDeadlines (
		@Header("X-Session-Id") sessionId: String,
		@Query("search[isClosed]") search: Boolean = true
	): Call<RetrofitResponse<RealmItemDeadline>>
	
	//APIv2
	@GET("deadlines")
	fun getExpiredDeadlines (
		@Header("X-Session-Id") sessionId: String,
		@Query("less[endDate]") less: Int,
		@Query("search[isClosed]") search: Boolean = false
	): Call<RetrofitResponse<RealmItemDeadline>>
	
	//APIv2
	@GET("deadlines")
	fun getExternalDeadlines (
		@Header("X-Session-Id") sessionId: String,
		@Query("externalSource") sourceId: String
	): Call<RetrofitResponse<RealmItemDeadline>>
	
	//APIv2
	@GET("deadlines/sources")
	fun getDeadlineSources (
		@Header("X-Session-Id") sessionId: String
	): Call<RetrofitResponse<RealmItemDeadlineSource>>
	
	//APIv2
	@POST("auth/{service}/")
	fun postUser (
		@Path("service") service: String,
		@Body body: RetrofitBodyLoginPassword
	): Call<RetrofitResponseAuth>
	
	//APIv2
	@POST("auth/vk")
	fun postVkUser (
		@Body body: RetrofitBodyToken
	): Call<RetrofitResponseAuth>
	
	//APIv2
	@POST("deadlines")
	fun postDeadline (
		@Header("X-Session-Id") sessionId: String,
		@Body body: RetrofitBodyDeadline
	): Call<RetrofitResponseDeadline>
	
	//APIv2
	@POST("deadlines/{deadlineId}/close")
	fun postDeadlineCloseEndpoint (
		@Path("deadlineId") deadlineId: String,
		@Header("X-Session-Id") sessionId: String
	): Call<RetrofitResponseDeadline>
	
	// APIv2
	@POST("auth/logout")
	fun postLogout (
		@Header("X-Session-Id") sessionId: String
	): Call<Any>
	
	//APIv2
	@PUT("me")
	fun putMe (
		@Header("X-Session-Id") sessionId: String,
		@Body body: RetrofitBodyMe
	): Call<RetrofitResponseMe>
	
	//APIv2
	@PUT("me")
	fun putMeServiceLogin (
		@Header("X-Session-Id") sessionId: String,
		@Body body: RetrofitBodyLoginPassword
	): Call<RetrofitResponseMe>

	@PUT("deadlines/{deadlineId}") //APIv2
	fun putDeadline (
		@Path("deadlineId") deadlineId: String,
		@Header("X-Session-Id") sessionId: String,
		@Body body: RetrofitBodyDeadline
	): Call<RetrofitResponseDeadline>
	
	//APIv2
	@DELETE("deadlines/{deadlineId}/close")
	fun deleteDeadlineCloseEndpoint (
		@Path("deadlineId") deadlineId: String,
		@Header("X-Session-Id") sessionId: String
	): Call<RetrofitResponseDeadline>
	
	//APIv2
	@DELETE("deadlines/{deadlineId}")
	fun deleteDeadline (
		@Path("deadlineId") deadlineId: String,
		@Header("X-Session-Id") sessionId: String
	): Call<RetrofitResponseDeadline>
}