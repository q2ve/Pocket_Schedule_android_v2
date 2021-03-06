package com.q2ve.pocketschedule2.ui.login.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.Observable
import com.q2ve.pocketschedule2.helpers.VKAuthCallbackSetter
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemMain
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment
import com.q2ve.pocketschedule2.ui.recyclerSelector.RecyclerSelectorPresenter
import com.q2ve.pocketschedule2.ui.recyclerSelector.RecyclerSelectorUploadingControllerUniversities

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginMethodSelectorViewModel: ViewModel() {
	private val router = LoginMethodSelectorRouter()
	private var bottomPopupContainer: BottomPopupContainerFragment? = null
	
	var errorMessage: Observable<Int?>? = Observable(null)
	var loadingSpinnerVisibility: Observable<Boolean>? = Observable(false)
	var universityName: Observable<String>? = Observable("")
	
	private var selectedUniversity: RealmItemUniversity? = null
	
	init {
		VKAuthCallbackSetter.callback = ::vkAuthCallback
	}
	
	private fun vkAuthCallback(token: String?) {
		if (token == null) {
			makeErrorMessage(R.string.vk_auth_unsuccessful)
			hideSpinner()
		} else {
			fun onError(errorType: ErrorType) {
				hideSpinner()
				when (errorType) {
					ErrorType.ValidationError -> makeErrorMessage(R.string.vk_auth_unsuccessful)
					ErrorType.NoInternetConnection -> Unit //This error will be displayed as toast
					else -> makeErrorMessage(R.string.server_error)
				}
			}
			fun onSuccess(mainObject: RealmItemMain) {
				val user = mainObject.currentUser
				if (user?.scheduleUser == null || user.university == null) {
					router.openScheduleUserPicker()
				}
				else router.goToCoreFragments()
			}
			Model(::onError).postVkUser(token, ::onSuccess)
		}
	}
	
	private fun showSpinner() { loadingSpinnerVisibility?.value = true }
	
	private fun hideSpinner() { loadingSpinnerVisibility?.value = false }
	
	private fun makeErrorMessage(stringId: Int) { errorMessage?.value = stringId }
	
	private fun removeErrorMessage() { errorMessage?.value = null }
	
	fun enterButtonPressed() {
		removeErrorMessage()
		if (selectedUniversity == null) makeErrorMessage(R.string.error_no_university_selected)
		else {
			val authorizationService = selectedUniversity!!.serviceName ?: ""
			val universityId = selectedUniversity!!._id
			router.openUniversityAuthScreen(authorizationService, universityId)
		}
//		else router.openUniversityAuthScreen(selectedUniversity, isFromOnboarding)
	}
	
	fun vkButtonPressed(activity: Activity) {
		removeErrorMessage()
		showSpinner()
		router.openVKAuthScreen(activity)
	}
	
	fun backButtonPressed(activity: Activity) {
		router.goBackToOnboarding(activity)
//		if (isFromOnboarding) {
//			router.goBackToOnboarding(isFromLogin = true)
//		} else {
//			router.goToCoreFragments(smthng)
//		}
	}
	
	fun universitySelectorPressed() {
		removeErrorMessage()
		router.openBottomPopupContainer(::placeSelector)
	}
	
	private fun placeSelector(fragment: BottomPopupContainerFragment) {
		bottomPopupContainer = fragment
		val container = fragment.binding.bottomPopupContainerContentContainer
		val uploadingController = RecyclerSelectorUploadingControllerUniversities(
			null,
			::onSelectorError
		)
		fun onItemClick(index: Int) { recyclerCallback(uploadingController.getItem(index)) }
		RecyclerSelectorPresenter(container, fragment, uploadingController, ::onItemClick)
			.placeRecycler()
	}
	
	private fun onSelectorError(errorType: ErrorType) {
		Log.e("LoginMethodSelectorViewModel.onSelectorError", errorType.toString())
		when (errorType) {
			ErrorType.ValidationError -> makeErrorMessage(R.string.server_error)
			ErrorType.UnknownServerError -> makeErrorMessage(R.string.server_error)
			ErrorType.NoInternetConnection -> Unit //This error will be displayed as toast
			else -> makeErrorMessage(R.string.an_error_has_occurred_try_again)
		}
		bottomPopupContainer?.animateExit()
	}
	
	private fun recyclerCallback(university: RealmItemUniversity?) {
		if (university == null) {
			Log.e("LoginMethodSelectorViewModel.recyclerCallback", "university == null")
			makeErrorMessage(R.string.an_error_has_occurred_try_again)
		} else {
			selectedUniversity = university
			universityName?.value = university.name ?: "-"
		}
		bottomPopupContainer?.animateExit()
	}
	
	fun onDestroyView() {
		//TODO("??????????????????, ?????????? ???? ??????")
		errorMessage = null
		loadingSpinnerVisibility = null
		universityName = null
	}
}