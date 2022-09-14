package com.q2ve.schedappv2.ui.scheduleUserPicker

import android.util.Log
import androidx.lifecycle.ViewModel
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.helpers.Observable
import com.q2ve.schedappv2.helpers.UserObserver
import com.q2ve.schedappv2.model.ErrorType
import com.q2ve.schedappv2.model.Model
import com.q2ve.schedappv2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.schedappv2.model.dataclasses.RealmItemUniversity
import com.q2ve.schedappv2.ui.popup.BottomPopupContainerFragment
import com.q2ve.schedappv2.ui.recyclerSelector.RecyclerSelectorPresenter
import com.q2ve.schedappv2.ui.recyclerSelector.RecyclerSelectorUploadingControllerGroups
import com.q2ve.schedappv2.ui.recyclerSelector.RecyclerSelectorUploadingControllerUniversities

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Запретить нажатия на поля после ентера")
class ScheduleUserPickerViewModel: ViewModel() {
	private val router = ScheduleUserPickerRouter()
	private var bottomPopupContainer: BottomPopupContainerFragment? = null
	
	var errorMessage: Observable<Int?>? = Observable(null)
	var loadingSpinnerVisibility: Observable<Boolean>? = Observable(false)
	var universityName: Observable<String>? = Observable("")
	var scheduleUserName: Observable<String>? = Observable("")
	
	private var selectedUniversity: RealmItemUniversity? = null
	private var selectedScheduleUser: RealmItemScheduleUser? = null
	
	fun universityFieldPressed() {
		removeErrorMessage()
		router.openBottomPopupContainer(
			R.string.university_choosing,
			false,
			::placeUniversitySelector
		)
	}
	
	private fun placeUniversitySelector(
		fragment: BottomPopupContainerFragment) {
		bottomPopupContainer = fragment
		val container = fragment.binding.bottomPopupContainerContentContainer
		val uploadingController = RecyclerSelectorUploadingControllerUniversities(
			null,
			::onSelectorError
		)
		fun onItemClick(index: Int) {
			universitySelectorCallback(uploadingController.getItem(index))
		}
		RecyclerSelectorPresenter(container, fragment, uploadingController, ::onItemClick)
			.placeRecycler()
	}
	
	private fun universitySelectorCallback(university: RealmItemUniversity?) {
		if (university == null) {
			Log.e(
				"ScheduleUserPickerViewModel.universitySelectorCallback",
				"university is null"
			)
			makeErrorMessage(R.string.an_error_has_occurred_try_again)
		} else {
			selectedScheduleUser = null
			scheduleUserName?.value = ""
			selectedUniversity = university
			universityName?.value = university.name ?: "-"
		}
		bottomPopupContainer?.animateExit()
	}
	
	fun scheduleUserFieldPressed() {
		removeErrorMessage()
		if (selectedUniversity == null) makeErrorMessage(R.string.error_no_university_selected)
		else router.openBottomPopupContainer(
			R.string.user_choosing,
			true,
			::placeScheduleUserSelector
		)
	}
	
	//TODO("КНОПКИ")
	private fun placeScheduleUserSelector(fragment: BottomPopupContainerFragment) {
		selectedUniversity?.let {
			bottomPopupContainer = fragment
			val container = fragment.binding.bottomPopupContainerContentContainer
			val uploadingController = RecyclerSelectorUploadingControllerGroups(
				null,
				::onSelectorError,
				selectedUniversity!!._id
			)
			fun onItemClick(index: Int) {
				scheduleUserSelectorCallback(uploadingController.getItem(index))
			}
			RecyclerSelectorPresenter(container, fragment, uploadingController, ::onItemClick)
				.placeRecycler()
		} ?: run {
			Log.e(
				"ScheduleUserPickerViewModel.placeScheduleUserSelector",
				"selectedUniversity is null"
			)
			onSelectorError(ErrorType.UnknownInternalError)
		}
	}
	
	private fun scheduleUserSelectorCallback(scheduleUser: RealmItemScheduleUser?) {
		if (scheduleUser == null) {
			Log.e(
				"ScheduleUserPickerViewModel.scheduleUserSelectorCallback",
				"scheduleUser is null"
			)
			makeErrorMessage(R.string.an_error_has_occurred_try_again)
		} else {
			selectedScheduleUser = scheduleUser
			scheduleUserName?.value = scheduleUser.name ?: "-"
		}
		bottomPopupContainer?.animateExit()
	}
	
	private fun onSelectorError(errorType: ErrorType) {
		Log.e("LoginMethodSelectorViewModel.onSelectorError", errorType.toString())
		onError(errorType)
		bottomPopupContainer?.animateExit()
	}
	
	fun enterButtonPressed() {
		removeErrorMessage()
		when {
			(selectedUniversity == null && selectedScheduleUser == null) -> {
				makeErrorMessage(R.string.error_nothing_selected)
			}
			(selectedUniversity == null) -> {
				makeErrorMessage(R.string.error_no_university_selected)
			}
			(selectedScheduleUser == null) -> {
				makeErrorMessage(R.string.error_no_schedule_user_selected)
			}
			else -> {
				showSpinner()
				val mainObject = UserObserver.getMainObject()
				val sessionId = mainObject.sessionId
				if (sessionId == null) {
					mainObject.scheduleUniversity = selectedUniversity
					mainObject.scheduleUser = selectedScheduleUser
					fun onError(errorType: ErrorType) {
						Log.e(
							"LoginMethodSelectorViewModel.enterButtonPressed()",
							errorType.toString()
						)
						makeErrorMessage(R.string.an_error_has_occurred_try_again)
						hideSpinner()
					}
					Model(::onError).updateMainObject(mainObject) { router.goToCoreFragments() }
				} else {
					Model(::onError).putMe(
						sessionId,
						selectedUniversity!!._id,
						selectedScheduleUser!!._id
					) { router.goToCoreFragments() }
				}
			}
		}
	}
	
	private fun onError(errorType: ErrorType) {
		hideSpinner()
		when (errorType) {
			ErrorType.RealmError -> {
				makeErrorMessage(R.string.an_error_has_occurred_try_again)
			}
			ErrorType.UnknownExternalError -> {
			makeErrorMessage(R.string.an_error_has_occurred_try_again)
			}
			ErrorType.UnknownInternalError -> {
				makeErrorMessage(R.string.an_error_has_occurred_try_again)
			}
			ErrorType.NoInternetConnection -> {
				//Do nothing. This error will be displayed as toast.
			}
			else -> makeErrorMessage(R.string.server_error)
		}
	}
	
	fun backButtonPressed() { router.goBack() }
	
	private fun showSpinner() { loadingSpinnerVisibility?.value = true }
	
	private fun hideSpinner() { loadingSpinnerVisibility?.value = false }
	
	private fun makeErrorMessage(stringId: Int) { errorMessage?.value = stringId }
	
	private fun removeErrorMessage() { errorMessage?.value = null }
	
	fun onDestroyView() {
		//TODO("Проверить, нужно ли это")
		errorMessage = null
		loadingSpinnerVisibility = null
		universityName = null
		scheduleUserName = null
	}
}