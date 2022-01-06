package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.model.realm.RealmIO
import com.q2ve.pocketschedule2.model.retrofit.RetrofitCalls

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class Model {
	fun testREMOVEit() {
		fun testCallback(foo: RealmItemUniversity?, bar: ErrorType?) {
			RealmIO().insertOrUpdate(foo!!)
		}
		fun testCallback2(foo: List<RealmItemUniversity>, bar: ErrorType?) {
			RealmIO().insertOrUpdate(foo)
		}
		RealmIO().copyFromRealm("1488", ::testCallback)
		RealmIO().copyFromRealm(::testCallback2)
		RealmIO().copyIndexedFromRealm(::testCallback2)
		RealmIO().insertOrUpdateWithIndexing(emptyList(), ::testCallback2)
	}
	
	fun getUniversitiesTEST(callback: (List<RealmItemUniversity>) -> Unit) {
		RetrofitCalls().getUniversities { objects: List<RealmItemUniversity>?, error: ErrorType? ->
			if (objects != null) {
				fun testCallback(foo: List<RealmItemUniversity>, bar: ErrorType?) {
					RealmIO().copyIndexedFromRealm<RealmItemUniversity> { list, errorType ->
						callback(ErrorChecker().checkUniversities(list))
					}
				}
				RealmIO().insertOrUpdateWithIndexing(objects, ::testCallback)
			}
		}
	}
	
	fun getUniversities(callback: (List<RealmItemUniversity>?, ErrorType?) -> Unit) {
		RetrofitCalls().getUniversities { objects, errorType ->
			if (errorType != null) {
				callback(null, errorType)
			} else {
				RealmIO().insertOrUpdateWithIndexing(objects ?: emptyList(), callback)
			}
		}
	}
	
	fun getGroups(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		callback: (List<RealmItemScheduleUser>?, ErrorType?) -> Unit
	) {
		RetrofitCalls().getGroups (offset, limit, university, query)
		{ objects: List<RealmItemScheduleUser>?, errorType: ErrorType? ->
			if (errorType != null) {
				callback(null, errorType)
			} else {
				RealmIO().insertOrUpdateWithIndexing(objects ?: emptyList(), callback)
			}
		}
	}
	
	fun getProfessors(
		offset: Int,
		limit: Int,
		university: String,
		query: String = "",
		callback: (List<RealmItemScheduleUser>?, ErrorType?) -> Unit
	) {
		RetrofitCalls().getProfessors (offset, limit, university, query)
		{ objects: List<RealmItemScheduleUser>?, errorType: ErrorType? ->
			if (errorType != null) {
				callback(null, errorType)
			} else {
				RealmIO().insertOrUpdateWithIndexing(objects ?: emptyList(), callback)
			}
		}
	}
}