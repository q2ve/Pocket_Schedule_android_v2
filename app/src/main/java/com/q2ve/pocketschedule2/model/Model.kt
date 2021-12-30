package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.model.realm.RealmIO
import com.q2ve.pocketschedule2.model.retrofit.RetrofitCallSender

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
		RetrofitCallSender().getUniversities { objects: List<RealmItemUniversity>?, error: ErrorType? ->
			if (objects != null) {
				fun testCallback(foo: List<RealmItemUniversity>, bar: ErrorType?) {
					RealmIO().copyIndexedFromRealm<RealmItemUniversity> { list, errorType ->
						callback(list)
					}
				}
				RealmIO().insertOrUpdateWithIndexing(objects, ::testCallback)
			}
		}
	}
}