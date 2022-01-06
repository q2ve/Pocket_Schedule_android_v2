package com.q2ve.pocketschedule2.model.realm

import android.util.Log
import com.q2ve.pocketschedule2.helpers.Constants
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemMain
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

class RealmIO {
	val config: RealmConfiguration = RealmConfiguration.Builder()
		.name(Constants.realmName)
		.allowQueriesOnUiThread(true)
		.allowWritesOnUiThread(true)
		.build()
		
	fun insertOrUpdate(
		inputObject: RealmObject,
		callback: ((RealmObject?, ErrorType?) -> Unit)? = null
	) {
		var output: RealmObject? = null
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				output = r.copyToRealmOrUpdate(inputObject)
			}, {
				if (callback != null) {
					callback(output, null)
				}
			}, {
				if (callback != null) {
					callback(null, ErrorType.RealmError)
					//TODO("Check possible throwable.")
					Log.e("RealmIO.insertOrUpdate single", it.toString())
				}
			}
		)
		realm.close()
	}
	
	fun insertOrUpdate(
		inputObjects: List<RealmObject>,
		callback: ((List<RealmObject>, ErrorType?) -> Unit)? = null
	) {
		var output: List<RealmObject> = emptyList()
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				output = r.copyToRealmOrUpdate(inputObjects)
			}, {
				if (callback != null) {
					callback(output, null)
				}
			}, {
				if (callback != null) {
					callback(emptyList(), ErrorType.RealmError)
					//TODO("Check possible throwable.")
					Log.e("RealmIO.insertOrUpdate multi", it.toString())
				}
			}
		)
		realm.close()
	}
	
	inline fun <reified T: RealmObject> copyFromRealm(
		id: String,
		noinline callback: ((T?, ErrorType?) -> Unit)
	) {
		Log.d("Test - Generic name is", T::class.java.simpleName)
		var output: T? = null
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val realmObject: T? = r.where(T::class.java).equalTo("_id", id).findFirst()
				output = if (realmObject == null) null
						 else r.copyFromRealm(realmObject)
			}, {
				callback(output, null)
			}, {
				callback(null, ErrorType.RealmError)
				//TODO("Check possible throwable.")
				Log.e("RealmIO.copyFromRealm single", it.toString())
			}
		)
		realm.close()
	}
	
	inline fun <reified T: RealmObject> copyFromRealm(
		noinline callback: ((List<T>, ErrorType?) -> Unit)
	) {
		Log.d("Test - Generic name is", T::class.java.simpleName)
		var output: List<T> = emptyList()
		val realm = Realm.getInstance(config)
		realm.executeTransactionAsync(
			{ r: Realm ->
				val realmObjects: List<T> = r.where(T::class.java).findAll()
				output = r.copyFromRealm(realmObjects)
			}, {
				callback(output, null)
			}, {
				callback(emptyList(), ErrorType.RealmError)
				//TODO("Check possible throwable.")
				Log.e("RealmIO.copyFromRealm multi", it.toString())
			}
		)
		realm.close()
	}
	
	/**
	 * This function is needed to index and add items we got from the server to the local database.
	 */
	inline fun <reified T: RealmObject> insertOrUpdateWithIndexing(
		inputObjects: List<RealmObject>,
		noinline callback: (List<T>, ErrorType?) -> Unit
	) {
		Log.d("Test - Generic name is", T::class.java.simpleName)
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
				callback(output, null)
			}, {
				callback(emptyList(), ErrorType.RealmError)
				//TODO("Check possible throwable.")
				Log.e("RealmIO.insertOrUpdateWithIndexing", it.toString())
			}
		)
		realm.close()
	}
	
	inline fun <reified T: RealmObject> copyIndexedFromRealm(
		noinline callback: ((List<T>, ErrorType?) -> Unit)
	) {
		Log.d("Test - Generic name is ", T::class.java.simpleName)
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
				callback(output, null)
			}, {
				callback(emptyList(), ErrorType.RealmError)
				//TODO("Check possible throwable.")
				Log.e("RealmIO.copyIndexedFromRealm", it.toString())
			}
		)
		realm.close()
	}
	
	fun getMainObject(): RealmItemMain {
		val realm = Realm.getInstance(config)
		var mainObject = RealmItemMain()
		realm.executeTransaction { r: Realm ->
			val foundMainObject = r.where(RealmItemMain::class.java)
									.equalTo("_id", "mainObject")
									.findFirst()
			if (foundMainObject == null) { r.insertOrUpdate(RealmItemMain()) }
			else {
				mainObject = foundMainObject
				Log.d("getMainObject foundMainObject in realm transaction", mainObject.toString())
			}
		}
		realm.close()
		Log.d("getMainObject before return", mainObject.toString())
		return mainObject
	}
	
	fun observeMainObject(setMainObject: (RealmItemMain) -> Unit) {
		val realm = Realm.getInstance(config)
		var mainObject: RealmItemMain? = null
		realm.executeTransaction { r: Realm ->
			mainObject = r.where(RealmItemMain::class.java)
							.equalTo("_id", "mainObject")
							.findFirst()
		}
		Log.d("observeMainObject", mainObject.toString())
		if (mainObject == null) {
			TODO("Надо придумать что-то с этим говном.")
		}
		mainObject?.addChangeListener { it: RealmItemMain ->
			Log.d("mainObjectChangeListener", it.toString())
			realm.executeTransaction { r: Realm ->
				setMainObject(r.copyFromRealm(it))
			}
		}
		realm.close()
	}
}