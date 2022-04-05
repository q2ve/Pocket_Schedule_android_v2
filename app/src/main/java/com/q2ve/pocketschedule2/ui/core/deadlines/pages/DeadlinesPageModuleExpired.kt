package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesPageModuleExpired(
	emptyLayoutId: Int,
	private val sessionId: String,
	onError: (ErrorType) -> Unit
): DeadlinesPageModuleBase(emptyLayoutId) {
	private val model = Model(onError)
	private val currentTime = (System.currentTimeMillis() / 1000).toInt()
	private lateinit var list: RealmResults<RealmItemDeadline>
	private var realmInstance: Realm? = null
	
	override fun getDeadlines() {
		model.getExpiredDeadlines(sessionId, currentTime, ::onDeadlinesLoaded)
	}
	
	private fun onDeadlinesLoaded(deadlines: List<RealmItemDeadline>) {
		val realmResultsSet = model.getExpiredDeadlinesRealmResults(currentTime)
		//list = changeListenerSet.list
		realmInstance = realmResultsSet.realmInstance
		inflateDeadlines(realmResultsSet.list)
	}
	
	override fun onDestroyView() { realmInstance?.close() }
}