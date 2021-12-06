package com.q2ve.pocketschedule2.ui.login.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.helpers.Observable

class LoginMethodSelectorViewModel: ViewModel() {
	var errorMessage: Observable<Int?>? = Observable(null)
	var loadingSpinnerVisibility: Observable<Boolean>? = Observable(false)
	var universityName: Observable<String>? = Observable("")
	
	init {
//		VkCallbackDestinationSetter.setDestination(this)
	}
	
	fun vkAuthCallback(token: String?) {
//		if (token == null) {
//			fragment.makeErrorMessage(false, R.string.vk_auth_unsuccessful)
//			fragment.removeSpinner()
//		} else {
//			ContentGetter(this).postVkUser(token)
//		}
	}
	
	fun contentGetterCallback(
//		objects: List<Any>?,
//		isError: Boolean,
//		errorType: ErrorType?,
//		escortingObject: Any?
	) {
//		if (isError) {
//			fragment.removeSpinner()
//			when (errorType) {
//				ErrorType.ValidationError -> {
//					fragment.makeErrorMessage(false, R.string.vk_auth_unsuccessful)
//				}
//				ErrorType.NoInternetConnection -> {
//					//Do nothing. This error will be displayed as toast
//				}
//				else -> {
//					fragment.makeErrorMessage(false, R.string.server_error)
//				}
//			}
//		} else {
//			val outputObject = objects!!.first() as RealmMainObject
//			val user = outputObject.currentUser!!
//			//Log.d("scheduleUser", outputObject.scheduleUser?.name.toString())
//			//Log.d("scheduleUniversity", outputObject.scheduleUniversity?.universityName.toString())
//			//Log.d("user.scheduleUser", user.scheduleUser?.name.toString())
//			//Log.d("user.university", user.university?.universityName.toString())
//			if (user.scheduleUser == null || user.university == null) {
//				PopupLoginPresenter(this)
//			} else {
//				parent.goToCoreFragments()
//			}
//
//		}
	}
	
	private fun makeErrorMessage(stringId: Int) {
		errorMessage?.value = stringId
	}
	
	private fun removeErrorMessage() {
		errorMessage?.value = null
	}
	
	fun enterButtonPressed() {
//		removeErrorMessage()
//		if (selectedUniversity == null) {
//			fragment.makeErrorMessage(false, R.string.error_no_university_selected)
//		}
//		else {
//			parent.goToSecondScreen(selectedUniversity!!, isFromOnboarding)
//		}
	}
	
	fun vkButtonPressed(activity: Activity) {
//		removeErrorMessage()
//		fragment.placeSpinner()
//		VK.login(activity, arrayListOf(VKScope.WALL, VKScope.OFFLINE))
	}
	
	fun backButtonPressed() {
//		if (isFromOnboarding) {
//			parent.goToOnboarding(isFromLogin = true)
//		} else {
//			parent.goToCoreFragments()
//		}
	}
	
	fun universitySelectorPressed() {
//		removeErrorMessage()
//		bottomMenuView.presenter = bottomMenuPresenter
//		FragmentReplacer.addFragment(R.id.login_navigation, bottomMenuView)
//		placeSelector()
	}
	
	private fun placeSelector() {
//		RecyclerPresenter(
//			this,
//			R.id.bottom_menu_recycler_container,
//			null,
//			IndexerItemType.Universities
//		)
	}
	
	fun bottomMenuClosed() {
		//Do nothing
	}
	
	fun recyclerCallback(
//		realmObject: RealmIdNameInterface,
//		contentType: IndexerItemType
	) {
//		selectedUniversity = realmObject as RealmUniversityObject
//		fragment.bindNewUniversityName(realmObject.getTheName())
//		bottomMenuView.exitAnimation()
	}
	
	fun onDestroyView() {
		//TODO("Проверить, нужно ли это")
		errorMessage = null
		loadingSpinnerVisibility = null
		universityName = null
	}
}