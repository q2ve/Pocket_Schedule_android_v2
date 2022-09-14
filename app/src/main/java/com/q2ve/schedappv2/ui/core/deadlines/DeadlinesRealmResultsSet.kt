package com.q2ve.schedappv2.ui.core.deadlines

import com.q2ve.schedappv2.model.dataclasses.RealmItemDeadline
import io.realm.Realm
import io.realm.RealmResults

class DeadlinesRealmResultsSet (
	val list: RealmResults<RealmItemDeadline>,
	val realmInstance: Realm
)