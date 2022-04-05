package com.q2ve.pocketschedule2.ui.settings

import android.app.Activity
import android.os.Handler
import android.util.Log
import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.Observable
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.helpers.hideKeyboard
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUniversity
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUser
import com.q2ve.pocketschedule2.ui.core.CoreScreens
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment
import com.q2ve.pocketschedule2.ui.recyclerSelector.*

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class SettingsViewModel: ViewModel() {
	private val router = SettingsRouter()
	private var bottomPopupContainer: BottomPopupContainerFragment? = null
	private var selectorController: RecyclerSelectorUploadingControllerBase? = null
	
	private var isInteractionAllowed = true
	private var selectedUniversityObject: RealmItemUniversity? = null
	
	var authorizationErrorMessage: Observable<Int?>? = null
	var selectionErrorMessage: Observable<Int?>? = null
	var selectedStartScreen: Observable<CoreScreens>? = null
	var isSpinnerPlaced: Observable<Boolean>? = null
	var selectedServiceUser: Observable<String>? = null
	var selectedUserName: Observable<String>? = null
	var selectedUserGroup: Observable<String>? = null
	var selectedScheduleUniversity: Observable<String>? = null
	var selectedScheduleUser: Observable<String?>? = null
	
	fun onCreateView(inflatingFunction: (RealmItemUser, CoreScreens) -> Unit) {
		val mainObject = UserObserver.getMainObject()
		val startScreen = CoreScreens.values()[mainObject.startScreen]
		
		authorizationErrorMessage = Observable(null)
		selectionErrorMessage = Observable(null)
		selectedStartScreen = Observable(startScreen)
		isSpinnerPlaced = Observable(false)
		selectedServiceUser = Observable("")
		selectedUserName = Observable("")
		selectedUserGroup = Observable("")
		selectedScheduleUniversity = Observable("")
		selectedScheduleUser = Observable("")
		
		val user = mainObject.currentUser
		val scheduleUser = user?.scheduleUser
		val university = user?.university
		if (scheduleUser != null && university != null && mainObject.sessionId != null) {
			selectedUniversityObject = university
			inflatingFunction(user, startScreen)
			this.selectedScheduleUniversity?.value = university.name
			this.selectedScheduleUser?.value = scheduleUser.name
		} else Handler().postDelayed({ router.openAuthorizationRequirement() }, 450)
		//Delay is necessary to open requirement after transition of the fragment is completed
	}
	
	fun onBackButtonPressed(activity: Activity) { router.goBack(activity) }
	
	fun onLogoutButtonPressed() { UserObserver.logoutUser(showNotification = false) }
	
	fun onEnterButtonPressed(login: String, password: String) {
		removeAuthorizationErrorMessage()
		when {
			login.isEmpty() && password.isEmpty() -> {
				makeAuthorizationErrorMessage(R.string.how_to_login_without)
			}
			login.isEmpty() -> makeAuthorizationErrorMessage(R.string.login_required)
			password.isEmpty() -> makeAuthorizationErrorMessage(R.string.password_required)
			else -> {
				blockInteraction()
				val sessionId = UserObserver.getMainObject().sessionId
				if (sessionId == null) {
					router.openAuthorizationRequirement()
				} else {
					fun onError(errorType: ErrorType) {
						allowInteraction()
						when (errorType) {
							ErrorType.RealmError -> {
								makeAuthorizationErrorMessage(R.string.an_error_has_occurred_try_again)
							}
							ErrorType.ValidationError -> {
								makeAuthorizationErrorMessage(R.string.validation_error)
							}
							ErrorType.NoInternetConnection -> {
								Unit //This error will be displayed as toast
							}
							else -> {
								makeAuthorizationErrorMessage(R.string.server_error)
							}
						}
					}
					fun onSuccess(user: RealmItemUser) {
						allowInteraction()
						val serviceLogin = user.serviceLogin
						val name = user.firstName + " " + user.lastName
						val group = user.scheduleUser?.name ?: ""
						if (serviceLogin == null) onError(ErrorType.UnknownServerError) else {
							selectedServiceUser?.value = serviceLogin
							selectedUserName?.value = name
							selectedUserGroup?.value = group
						}
					}
					
					Model(::onError).putMeServiceLogin(sessionId, login, password, ::onSuccess)
				}
			}
		}
		
	}
	
	fun onStartScreenSelected(screen: CoreScreens) {
		val mainObject = UserObserver.getMainObject()
		mainObject.startScreen = if (screen == CoreScreens.Default) 1 else screen.ordinal
		Model{ }.updateMainObject(mainObject) { }
		selectedStartScreen?.value = screen
	}
	
	fun onUniversitySelectorPressed() {
		removeSelectionErrorMessage()
		router.openBottomPopupContainer(R.string.university_choosing, false, ::placeUniversitySelector)
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
			Log.e("SettingsViewModel.universitySelectorCallback", "university is null")
			makeSelectionErrorMessage(R.string.an_error_has_occurred_try_again)
		} else {
			selectedUniversityObject = university
			selectedScheduleUniversity?.value = university.name
			selectedScheduleUser?.value = null
		}
		bottomPopupContainer?.animateExit()
	}
	
	fun onScheduleUserSelectorPressed() {
		removeSelectionErrorMessage()
		if (selectedUniversityObject == null) {
			makeSelectionErrorMessage(R.string.error_no_university_selected)
		}
		else {
			router.openBottomPopupContainer(R.string.user_choosing, true, ::onBottomMenuOpened)
		}
	}
	
	private fun onBottomMenuOpened(fragment: BottomPopupContainerFragment) {
		bottomPopupContainer = fragment
		placeContentTypeButtons()
		placeSearchField()
		placeGroupSelector()
	}
	
	private fun placeContentTypeButtons() {
		val bottomMenu = bottomPopupContainer
		if (bottomMenu != null) {
			val container = bottomMenu.binding.bottomPopupContainerContentContainer
			val inflater = bottomMenu.layoutInflater
			val resources = bottomMenu.resources
			val theme = bottomMenu.requireActivity().theme
			val textColor = resources.getColor(R.color.colorLightGray1, theme)
			val backgroundColor = resources.getColor(R.color.colorPlaqueBackground, theme)
			val selectionTextColor = resources.getColor(R.color.colorBlue, theme)
			val selectionBackgroundColor = resources.getColor(R.color.colorBlueLight, theme)
			ContentTypeButtonsPresenter().placeButtons(
				container,
				inflater,
				textColor,
				backgroundColor,
				selectionTextColor,
				selectionBackgroundColor,
				::placeGroupSelector,
				::placeProfessorSelector
			)
		}
	}

	private fun placeSearchField() {
		val bottomMenu = bottomPopupContainer
		if (bottomMenu != null) {
			val container = bottomMenu.binding.bottomPopupContainerContentContainer
			val inflater = bottomMenu.layoutInflater
			fun searchItems(query: String) { selectorController?.searchItems(query) }
			SearchFieldPresenter().placeSearchField(
				container,
				inflater,
				::searchItems,
				bottomMenu::hideKeyboard
			)
		}
	}
	
	private fun placeGroupSelector() {
		selectedUniversityObject?.let { university ->
			bottomPopupContainer?.let { fragment ->
				val container = fragment.binding.bottomPopupContainerContentContainer
				if (container.childCount > 2) {
					container.removeView(container.getChildAt(container.childCount - 1))
				}
				val uploadingController = RecyclerSelectorUploadingControllerGroups(
					null,
					::onSelectorError,
					university._id
				)
				selectorController = uploadingController
				fun onItemClick(index: Int) {
					scheduleUserSelectorCallback(uploadingController.getItem(index))
				}
				RecyclerSelectorPresenter(container, fragment, uploadingController, ::onItemClick)
					.placeRecycler()
			}
		} ?: run {
			Log.e("SettingsViewModel.placeScheduleUserSelector", "selectedUniversity is null")
			onSelectorError(ErrorType.UnknownInternalError)
		}
	}
	
	private fun placeProfessorSelector() {
		selectedUniversityObject?.let { university ->
			bottomPopupContainer?.let { fragment ->
				val container = fragment.binding.bottomPopupContainerContentContainer
				if (container.childCount > 2) {
					container.removeView(container.getChildAt(container.childCount - 1))
				}
				val uploadingController = RecyclerSelectorUploadingControllerProfessors(
					null,
					::onSelectorError,
					university._id
				)
				selectorController = uploadingController
				fun onItemClick(index: Int) {
					scheduleUserSelectorCallback(uploadingController.getItem(index))
				}
				RecyclerSelectorPresenter(container, fragment, uploadingController, ::onItemClick)
					.placeRecycler()
			}
		} ?: run {
			Log.e("SettingsViewModel.placeScheduleUserSelector", "selectedUniversity is null")
			onSelectorError(ErrorType.UnknownInternalError)
		}
	}
	
	private fun scheduleUserSelectorCallback(scheduleUser: RealmItemScheduleUser?) {
		if (scheduleUser == null) {
			Log.e("SettingsViewModel.scheduleUserSelectorCallback", "scheduleUser is null")
			makeSelectionErrorMessage(R.string.an_error_has_occurred_try_again)
		} else {
			this.selectedScheduleUser?.value = scheduleUser.name
			val university = selectedUniversityObject
			if (university == null) {
				makeSelectionErrorMessage(R.string.error_no_university_selected)
			} else {
				val sessionId = UserObserver.getMainObject().sessionId
				if (sessionId == null) router.openAuthorizationRequirement() else {
					putMe(sessionId, university, scheduleUser)
				}
			}
		}
		bottomPopupContainer?.animateExit()
	}
	
	private fun onSelectorError(errorType: ErrorType) {
		Log.e("SettingsViewModel.onSelectorError", errorType.toString())
		when (errorType) {
			ErrorType.RealmError -> {
				makeSelectionErrorMessage(R.string.an_error_has_occurred_try_again)
			}
			ErrorType.UnknownExternalError -> {
				makeSelectionErrorMessage(R.string.an_error_has_occurred_try_again)
			}
			ErrorType.UnknownInternalError -> {
				makeSelectionErrorMessage(R.string.an_error_has_occurred_try_again)
			}
			ErrorType.NoInternetConnection -> {
				//Do nothing. This error will be displayed as toast.
			}
			else -> makeSelectionErrorMessage(R.string.server_error)
		}
		bottomPopupContainer?.animateExit()
	}
	
	private fun putMe(
		sessionId: String,
		university: RealmItemUniversity,
		scheduleUser: RealmItemScheduleUser
	) {
		fun onError(errorType: ErrorType) {
			Log.e("SettingsViewModel.putMe()", errorType.toString())
			makeSelectionErrorMessage(R.string.an_error_has_occurred_try_again)
			this.selectedScheduleUser?.value = null
			allowInteraction()
		}
		blockInteraction()
		Model(::onError).putMe(sessionId, university._id, scheduleUser._id) { allowInteraction() }
	}
	
	private fun allowInteraction() {
		removeSpinner()
		isInteractionAllowed = true
	}

	private fun blockInteraction() {
		showSpinner()
		isInteractionAllowed = false
	}
	
	private fun showSpinner() { isSpinnerPlaced?.value = true }
	
	private fun removeSpinner() { isSpinnerPlaced?.value = false }
	
	private fun makeAuthorizationErrorMessage(stringId: Int) {
		authorizationErrorMessage?.value = stringId
	}
	
	private fun removeAuthorizationErrorMessage() {
		authorizationErrorMessage?.value = null
	}
	
	private fun makeSelectionErrorMessage(stringId: Int) {
		selectionErrorMessage?.value = stringId
	}
	
	private fun removeSelectionErrorMessage() {
		selectionErrorMessage?.value = null
	}
	
	fun onDestroyView() {
		authorizationErrorMessage = null
		selectionErrorMessage = null
		selectedStartScreen = null
		isSpinnerPlaced = null
		selectedServiceUser = null
		selectedUserName = null
		selectedUserGroup = null
		selectedScheduleUniversity = null
		selectedScheduleUser = null
	}
}