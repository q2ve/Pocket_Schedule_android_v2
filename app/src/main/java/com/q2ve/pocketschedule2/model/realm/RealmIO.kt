package com.q2ve.pocketschedule2.model.realm

import android.util.Log
import com.q2ve.pocketschedule2.helpers.Constants
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemMain
import com.q2ve.pocketschedule2.ui.core.deadlines.DeadlinesRealmResultsSet
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.Sort
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty


/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RealmIO(val onError: ((ErrorType) -> Unit)? = null) {
	
	val config: RealmConfiguration = RealmConfiguration.Builder()
		.name(Constants.realmName)
		.allowQueriesOnUiThread(true)
		.allowWritesOnUiThread(true)
		.build()
		
	fun <T: RealmObject> insertOrUpdate(
		inputObject: T,
		onSuccess: ((T) -> Unit)? = null
	) {
		var output: T? = null
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val objectInRealm = r.copyToRealmOrUpdate(inputObject)
				output = r.copyFromRealm(objectInRealm)
			}, {
				if (output != null) onSuccess?.let { it(output!!) }
				else onError?.let { it(ErrorType.RealmError) }
			}, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.insertOrUpdate single", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	fun <T: RealmObject> insertOrUpdate(
		inputObjects: List<T>,
		onSuccess: ((List<T>) -> Unit)? = null,
	) {
		var output: List<T> = emptyList()
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val objectsInRealm = r.copyToRealmOrUpdate(inputObjects)
				output = r.copyFromRealm(objectsInRealm)
			}, {
				onSuccess?.let { it(output) }
			}, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.insertOrUpdate multi", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	inline fun <reified T: RealmObject> copyFromRealm(
		id: String,
		noinline onSuccess: ((T) -> Unit)
	) {
		var output: T? = null
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val realmObject: T? = r.where(T::class.java).equalTo("_id", id).findFirst()
				output = if (realmObject == null) null
						 else r.copyFromRealm(realmObject)
			}, {
				if (output != null) onSuccess(output!!)
				else onError?.let { it(ErrorType.RealmError) }
			}, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.copyFromRealm single", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	inline fun <reified T: RealmObject> copyFromRealm(
		noinline onSuccess: ((List<T>) -> Unit)
	) {
		var output: List<T> = emptyList()
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val realmObjects: List<T> = r.where(T::class.java).findAll()
				output = r.copyFromRealm(realmObjects)
			}, {
				onSuccess(output)
			}, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.copyFromRealm multi", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	/**
	 * This function is needed to index and add items we got from the server to the local database.
	 */
	inline fun <reified T: RealmObject> insertOrUpdateWithIndexing(
		inputObjects: List<RealmObject>,
		noinline onSuccess: (List<T>) -> Unit
	) {
		var output: List<T> = emptyList()
		val className = T::class.java.simpleName
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val oldIndexItems = r.where(IndexItem::class.java)
									 .equalTo("indexedObjectClass", className)
									 .findAll()
				oldIndexItems.deleteAllFromRealm()
				
				inputObjects.forEach { realmObject ->
					r.copyToRealmOrUpdate(realmObject)
					val countOfIndexedItems = r.where(IndexItem::class.java)
								 .equalTo("indexedObjectClass", className)
								 .findAll()
								 .size
					/**
					 * Index items needs to get the objects from DB
					 * in the correct order they came from the server.
					 */
					val indexItem = IndexItem()
					indexItem.index = countOfIndexedItems
					indexItem.indexedObjectClass = className
					/**
					 * A Great Reflexive Magic that finds right field in index item
					 * and fills it with the inputted object.
					 * It's necessary for this code to work with objects of any classes
					 * that i might add in the future.
					 */
					val fields = IndexItem::class.members
//					fields.forEach {
//						if (it.returnType.classifier.toString() == T::class.java.toString()) {
//							if (it.returnType.classifier is ЧТО-ТО)
//							Log.e("CURRENT TEST - СРАБОТАЛО!!!!!!!!!!!",  "-------------------------------------------")
//						}
//					}
					val field = fields.find {
						it.returnType.classifier.toString() == T::class.java.toString()
					}
					if (field is KMutableProperty) {
						field.setter.call(indexItem, realmObject)
					}
					r.insertOrUpdate(indexItem)
				}
				/**
				 * Sorting objects from DB with index items (also from DB).
				 */
				val indexObjectsSortedList = r.where(IndexItem::class.java)
									   .equalTo("indexedObjectClass", className)
									   .sort("index", Sort.ASCENDING)
									   .findAll()
				val sortedList: MutableList<T> = emptyList<T>().toMutableList()
				indexObjectsSortedList.forEach { indexItem ->
					val fields = IndexItem::class.members
					val field = fields.find {
						it.returnType.classifier.toString() == T::class.java.toString()
					}
					if (field is KProperty) {
						sortedList += field.getter.call(indexItem) as T
					}
				}
				/**
				 * Copying objects from DB. Now they're not related to DB.
				 */
				output = r.copyFromRealm(sortedList)
			}, {
				onSuccess(output)
			}, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.insertOrUpdateWithIndexing", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	inline fun <reified T: RealmObject> copyIndexedFromRealm(
		noinline onSuccess: ((List<T>) -> Unit)
	) {
		var output: List<T> = emptyList()
		val className = T::class.java.simpleName
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				/**
				 * Sorting objects from DB with index items (also from DB).
				 */
				val indexObjectsSortedList: List<IndexItem> = r.where(IndexItem::class.java)
					.equalTo("indexedObjectClass", className)
					.sort("index", Sort.ASCENDING)
					.findAll()
				val sortedList: MutableList<T> = emptyList<T>().toMutableList()
				indexObjectsSortedList.forEach { indexItem ->
					val fields = IndexItem::class.members
					val field = fields.find {
						it.returnType.classifier.toString() == T::class.java.toString()
					}
					if (field is KMutableProperty<*>) {
						sortedList += field.getter.call(indexItem) as T
					}
				}
				/**
				 * Copying objects from DB. Now they're not related to DB.
				 */
				output = r.copyFromRealm(sortedList)
			}, {
				onSuccess(output)
			}, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.copyIndexedFromRealm", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	fun <T: RealmObject> delete(
		inputObject: T,
		onSuccess: (() -> Unit)? = null
	) {
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val objectInRealm = r.copyToRealmOrUpdate(inputObject)
				objectInRealm.deleteFromRealm()
			}, { onSuccess?.let { it() } }, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.delete single", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	fun getMainObject(): RealmItemMain {
		val realm = Realm.getInstance(config)
		var mainObject = RealmItemMain()
		realm.executeTransaction { r: Realm ->
			val foundMainObject = r.where(RealmItemMain::class.java)
									.equalTo("_id", Constants.mainObjectId)
									.findFirst()
			if (foundMainObject == null) { r.insertOrUpdate(RealmItemMain()) }
			else {
				mainObject = r.copyFromRealm(foundMainObject)
			}
		}
		realm.close()
		return mainObject
	}
	
	fun resetMainObject() {
		val realm = Realm.getInstance(config)
		realm.executeTransaction { r: Realm ->
			val foundMainObject = r.where(RealmItemMain::class.java)
				.equalTo("_id", Constants.mainObjectId)
				.findFirst()
			if (foundMainObject != null) { r.insertOrUpdate(RealmItemMain()) }
		}
		realm.close()
	}
	
	//TODO("Переделать и проверить")
	fun observeMainObject(setMainObject: (RealmItemMain) -> Unit) {
		val realm = Realm.getInstance(config)
		var mainObject: RealmItemMain? = null
		realm.executeTransaction { r: Realm ->
			mainObject = r.where(RealmItemMain::class.java)
							.equalTo("_id", Constants.mainObjectId)
							.findFirst()
		}
		if (mainObject == null) {
			TODO("Надо придумать что-то с этим говном.")
		}
		mainObject?.addChangeListener { it: RealmItemMain ->
			realm.executeTransaction { r: Realm ->
				setMainObject(r.copyFromRealm(it))
			}
		}
		realm.close()
	}
	
	/**
	 * Realm instance must be closed when it becomes unnecessary.
	 */
	fun getDeadlinesRealmResultsSet(
		isClosed: Boolean,
		limitDate: Int? = null
	): DeadlinesRealmResultsSet {
		val realm = Realm.getInstance(config)
		val list = if (limitDate == null) {
			realm.where(RealmItemDeadline::class.java)
				.equalTo("isExternal", false)
				.equalTo("isClosed", isClosed)
				.sort("title", Sort.ASCENDING)
				.findAll()
		} else {
			realm.where(RealmItemDeadline::class.java)
				.equalTo("isExternal", false)
				.equalTo("isClosed", isClosed)
				.lessThan("endDate", limitDate)
				.sort("title", Sort.ASCENDING)
				.findAll()
		}
		return DeadlinesRealmResultsSet(list, realm)
	}
	
	fun deleteDeadlines(
		isClosed: Boolean,
		limitDate: Int? = null,
		onSuccess: (() -> Unit)? = null
	) {
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val list = if (limitDate == null) {
					r.where(RealmItemDeadline::class.java)
						.equalTo("isExternal", false)
						.equalTo("isClosed", isClosed)
						.findAll()
				} else {
					r.where(RealmItemDeadline::class.java)
						.equalTo("isExternal", false)
						.equalTo("isClosed", isClosed)
						.lessThan("endDate", limitDate)
						.findAll()
				}
				list.deleteAllFromRealm()
			}, { onSuccess?.let { it() } }, { t: Throwable ->
				onError?.let {
					//TODO("Check possible throwable.")
					Log.e("RealmIO.deleteDeadlines", t.toString())
					it(ErrorType.RealmError)
				}
			}
		)
		realm.close()
	}
	
	fun stopObservingAllDeadlines() {
		Log.e("stopObservingAllDeadlines()", "Gotcha")
		val realm = Realm.getInstance(config)
		realm.where(RealmItemDeadline::class.java).findAll().removeAllChangeListeners()
		realm.close()
	}
}