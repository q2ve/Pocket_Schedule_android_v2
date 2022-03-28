package com.q2ve.pocketschedule2.ui.core.deadlines

import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import io.realm.Realm
import io.realm.RealmResults

class DeadlinesChangeListenerSet (
	val list: RealmResults<RealmItemDeadline>,
	val realmInstance: Realm
)