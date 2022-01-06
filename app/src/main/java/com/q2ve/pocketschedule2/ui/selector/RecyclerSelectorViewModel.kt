package com.q2ve.pocketschedule2.ui.selector
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import com.q2ve.pocketschedule2.R
//import com.q2ve.pocketschedule2.model.realm.RealmNameInterface
//
///**
// * Created by Denis Shishkin
// * qwq2eq@gmail.com
// */
//
//interface RecyclerPresenterInterface {
//	fun recyclerCallback(realmObject: RealmNameInterface, contentType: IndexerItemType)
//}
//
////TODO(Saving objects to DB after selection. Needs to offline functionality)
//class RecyclerSelectorViewModel(
//	private val parent: RecyclerPresenterInterface,
//	private val recyclerFrame: Int,
//	private val university: String?,
//	private var contentType: RecyclerSelectorContentType = RecyclerSelectorContentType.Groups
//): ViewModel(), RecyclerFragmentInterface, RecyclerContentTypeButtonsInterface {
//
//	private lateinit var recycler: RecyclerSelectorFragment
//	private var isRecyclerPlaced = false
//	private var searchFlag = false
//	private var itemAddingFlag = false
//	private var offset = 0
//	private val limit = 20
//	private var query = ""
//
//	init {
//		if (contentType != RecyclerSelectorContentType.Universities && contentType != RecyclerSelectorContentType.Subjects) {
//			val buttons = RecyclerContentTypeButtons(this)
//			FragmentReplacer.addFragment(R.id.bottom_menu_buttons_container, buttons)
//		}
//		getItems()
//	}
//
//	private fun getItems() {
//		when (contentType) {
//			RecyclerSelectorContentType.Groups -> {
//				ContentGetter(this).getGroups(offset, limit, university!!, query)
//			}
//			RecyclerSelectorContentType.Professors -> {
//				ContentGetter(this).getProfessors(offset, limit, university!!, query)
//			}
//			RecyclerSelectorContentType.Universities -> {
//				ContentGetter(this).getUniversities()
//			}
//			RecyclerSelectorContentType.Subjects -> {
//				val scheduleUser = UserObserver.getMainObject()?.scheduleUser
//				val university = UserObserver.getMainObject()?.scheduleUniversity
//				if (scheduleUser != null && university != null) {
//					ContentGetter(this).getLessons(university._id, scheduleUser._id)
//				}
//			}
//			else -> { /*TODO(Other content types and exception handling)*/ }
//		}
//	}
//
//	private fun placeRecycler(items: List<RealmNameInterface>) {
//		val hideSearchField = (contentType == IndexerItemType.Universities || contentType == IndexerItemType.Subjects)
//		recycler = RecyclerSelectorFragment(this, items, hideSearchField)
//		FragmentReplacer.replaceFragment(recyclerFrame, recycler)
//		isRecyclerPlaced = true
//	}
//
//	private fun removeRecycler() {
//		FragmentReplacer.removeFragment(recycler)
//	}
//
//	override fun onRecyclerItemClicked(realmObject: RealmNameInterface) {
//		parent.recyclerCallback(realmObject, contentType)
//	}
//
//	override fun searchItems(name: String) {
//		Log.d("Search this", name)
//		query = name
//		dropOffset()
//		getItems()
//	}
//
//	override fun uploadMoreItems() {
//		if (contentType != IndexerItemType.Universities && contentType != IndexerItemType.Subjects) {
//			itemAddingFlag = true
//			increaseOffset()
//			getItems()
//		}
//	}
//
//	override fun contentGetterCallback(
//		objects: List<Any>?,
//		isError: Boolean,
//		errorType: ErrorType?,
//		escortingObject: Any?
//	) {
//		if (isError) {
//			when (errorType) {
//				ErrorType.NoInternetConnection -> {
//					//Do nothing. This error will be displayed as toast
//				}
//				ErrorType.UnknownServerError -> {
//					Log.e("RecyclerPresenter", errorType.toString())
//					recycler.makeErrorMessage(R.string.server_error)
//				}
//				else -> {
//					Log.e("RecyclerPresenter", errorType.toString())
//					recycler.makeErrorMessage(R.string.server_error)
//				}
//			}
//		} else {
//			if (contentType == IndexerItemType.Subjects) {
//				val shitFix = RealmSubjectObject("removeSubject", "--------")
//				val subjects: MutableList<RealmSubjectObject> = listOf(shitFix).toMutableList()
//				(objects as List<RealmLessonObject>).forEach {
//					if (!(subjects.contains(it.subject)) && it.subject != null) {
//						subjects += it.subject!!
//					}
//				}
//				placeRecycler(subjects)
//				searchFlag = true
//			} else {
//				if (itemAddingFlag) {
//					itemAddingFlag = false
//					recycler.addItems(objects as List<RealmNameInterface>)
//				} else {
//					if (searchFlag) {
//						recycler.updateData(objects as List<RealmNameInterface>)
//					} else {
//						placeRecycler(objects as List<RealmNameInterface>)
//						searchFlag = true
//					}
//				}
//			}
//
//		}
//	}
//
//	private fun increaseOffset() {
//		offset += 20
//	}
//
//	private fun dropOffset() {
//		offset = 0
//	}
//
//	private fun dropFlags() {
//		searchFlag = false
//		itemAddingFlag = false
//	}
//
//	override fun contentTypeButtonsCallback(newContentType: IndexerItemType) {
//		if (isRecyclerPlaced) {
//			contentType = newContentType
//			removeRecycler()
//			dropOffset()
//			dropFlags()
//			getItems()
//		}
//	}
//}