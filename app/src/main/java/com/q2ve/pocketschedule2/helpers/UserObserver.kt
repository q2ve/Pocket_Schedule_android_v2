package com.q2ve.pocketschedule2.helpers

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemMain
import com.q2ve.pocketschedule2.model.realm.RealmIO

object UserObserver {
	private lateinit var mainObject: RealmItemMain
	
	fun configure() {
		setMainObject(RealmIO().getMainObject())
		RealmIO().observeMainObject(::setMainObject)
	}
	
	private fun setMainObject(newMainObject: RealmItemMain) { mainObject = newMainObject }
	
	fun getMainObject() = mainObject
}