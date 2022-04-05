package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import io.realm.Realm

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesPageModuleClosed(
	emptyLayoutId: Int,
	private val sessionId: String,
	onError: (ErrorType) -> Unit
): DeadlinesPageModuleBase(emptyLayoutId) {
	private val model = Model(onError)
	//private lateinit var list: RealmResults<RealmItemDeadline>
	private var realmInstance: Realm? = null
	
	override fun getDeadlines() {
		model.getClosedDeadlines(sessionId, ::onDeadlinesLoaded)
	}
	
	private fun onDeadlinesLoaded(deadlines: List<RealmItemDeadline>) {
		val realmResultsSet = model.getClosedDeadlinesRealmResults()
		//list = changeListenerSet.list
		realmInstance = realmResultsSet.realmInstance
		inflateDeadlines(realmResultsSet.list)
	}
	
	override fun onDestroyView() { realmInstance?.close() }
}