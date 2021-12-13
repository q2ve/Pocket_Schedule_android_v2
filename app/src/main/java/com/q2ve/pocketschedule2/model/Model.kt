package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.model.realm.RealmIO

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
		RealmIO().insertOrUpdateWithIndexer(emptyList(), ::testCallback2)
	}
}