package com.q2ve.pocketschedule2.ui.core.schedule

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.Observable
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.helpers.hideKeyboard
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.ModelInterface
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemLesson
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemScheduleUser
import com.q2ve.pocketschedule2.ui.buttonBar.ButtonBarController
import com.q2ve.pocketschedule2.ui.buttonBar.ButtonBarView
import com.q2ve.pocketschedule2.ui.buttonBar.ButtonBarViewModel
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment
import com.q2ve.pocketschedule2.ui.recyclerSelector.*
import javax.inject.Inject

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleViewModel @Inject constructor(): ViewModel() {
	private val extensions = ScheduleViewModelExtensions()
	private val router = ScheduleRouter()
	@Inject lateinit var model: ModelInterface
	
	private var bottomPopupContainer: BottomPopupContainerFragment? = null
	private var selectorController: RecyclerSelectorUploadingControllerBase? = null
	private lateinit var buttonBarController: ButtonBarController
	
	private lateinit var lessons: List<RealmItemLesson>
	
	private var selectionThrottleFlag = true
	private var isButtonBarClickable: Boolean = false
		set(isClickable) {
			if (this::buttonBarController.isInitialized) buttonBarController.setClickable(isClickable)
			field = isClickable
		}
	
	var lessonsViews: Observable<Array<Fragment>?>? = null
	var selectedWeekParity: Observable<ScheduleWeekParity>? = null
	var selectedPage: Observable<Int>? = null
	var errorMessage: Observable<Int?>? = null
	
	//Fragment's and views' callbacks
	
	fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup,
		resources: Resources,
		theme: Resources.Theme
	) {
		
		model.hashCode()
		throttleButtonBarButtonSelection()
		restrictButtonBarClicks()
		lessonsViews = Observable(null)
		selectedWeekParity = Observable(ScheduleWeekParity.Odd)
		selectedPage = Observable(0)
		errorMessage = Observable(null)
		
		val days: MutableList<ButtonBarViewModel> = emptyList<ButtonBarViewModel>().toMutableList()
		val currentDay = extensions.getCurrentWeekday()
		val dayNumber = currentDay.ordinal
		enumValues<ScheduleWeekdays>().forEachIndexed { index, it ->
			days += if (index == dayNumber) {
				ButtonBarViewModel(
					text = resources.getString(it.getShortWeekdayName()),
					textColor = ButtonBarViewModel("").selectionTextColor
				)
			} else ButtonBarViewModel(resources.getString(it.getShortWeekdayName()))
		}
		buttonBarController = ButtonBarController(
			::onButtonBarItemSelected,
			days,
			null,
			false
		)
		val buttonBar = ButtonBarView(buttonBarController).createView(
			inflater,
			container,
			resources,
			theme
		)
		container.addView(buttonBar)
	}
	
	fun onViewCreated() { getLessons() }
	
	fun onWaitingSpinnerPlacing() {
		throttleButtonBarButtonSelection()
	}
	
	fun onFilterButtonPressed() { openBottomPopupMenu() }
	
	fun onSettingsButtonPressed() {
		val mainObject = UserObserver.getMainObject()
		if (mainObject.currentUser == null || mainObject.sessionId == null) {
			router.openAuthorizationRequirement()
		} else router.openSettings()
	}
	
	fun onParityButtonPressed() {
		if (isButtonBarClickable && selectedWeekParity != null) {
			removeErrorMessage()
			restrictButtonBarClicks()
			when (selectedWeekParity?.value) {
				ScheduleWeekParity.Odd -> selectedWeekParity?.value = ScheduleWeekParity.Even
				ScheduleWeekParity.Even -> selectedWeekParity?.value = ScheduleWeekParity.Odd
				else -> { /*Do Nothing*/ }
			}
			placeSchedule(
				extensions.sortScheduleByParity(selectedWeekParity?.value, lessons),
				buttonBarController.currentVisualSelection.value
			)
		}
	}
	
	fun onPagerPageSelected(index: Int) {
		if (!selectionThrottleFlag) selectButtonBarItem(index)
		unthrottleButtonBarButtonSelection()
	}
	
	fun onDestroyView() {
		lessonsViews = null
		selectedWeekParity = null
		selectedPage = null
		errorMessage = null
	}
	
	private fun onButtonBarItemSelected(selectedItem: Int) {
		if (isButtonBarClickable) {
			removeErrorMessage()
			throttleButtonBarButtonSelection()
			selectPage(selectedItem)
		}
	}
	
	//View changing
	
	private fun placeSchedule(
		sortedSchedule: Array<RealmItemLesson>,
		selectSpecificDay: Int? = null
	) {
		val scheduleModules = extensions.inflateModuleFragments(sortedSchedule)
		throttleButtonBarButtonSelection()
		lessonsViews?.value = scheduleModules
		Handler(Looper.getMainLooper()).postDelayed({
			val currentDay = extensions.getCurrentWeekday()
			val dayNumber = selectSpecificDay ?: currentDay.ordinal
			selectPage(dayNumber)
			Handler(Looper.getMainLooper()).postDelayed({
				allowButtonBarClicks()
				selectButtonBarItem(dayNumber)
			}, 400)
		}, 320)
	}
	
	private fun selectButtonBarItem(index: Int) { buttonBarController.selectButton(index) }
	
	private fun selectPage(index: Int) { selectedPage?.value = index }
	
	private fun makeErrorMessage(stringId: Int) { errorMessage?.value = stringId }
	
	private fun removeErrorMessage() { errorMessage?.value = null }
	
	private fun openBottomPopupMenu() {
		removeErrorMessage()
		router.openBottomPopupContainer(R.string.user_choosing, ::onBottomMenuPlaced)
	}
	
	private fun onBottomMenuPlaced(fragment: BottomPopupContainerFragment) {
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
		val university = UserObserver.getMainObject().scheduleUniversity
		if (university == null) router.openScheduleUserPicker() else {
			val uploadingController = RecyclerSelectorUploadingControllerGroups(
				null,
				::onScheduleUserSelectorError,
				university._id
			)
			selectorController = uploadingController
			placeScheduleUserSelector(uploadingController) { index ->
				scheduleUserSelectorCallback(uploadingController.getItem(index))
			}
		}
	}
	
	private fun placeProfessorSelector() {
		val university = UserObserver.getMainObject().scheduleUniversity
		if (university == null) router.openScheduleUserPicker() else {
			val uploadingController = RecyclerSelectorUploadingControllerProfessors(
				null,
				::onScheduleUserSelectorError,
				university._id
			)
			selectorController = uploadingController
			placeScheduleUserSelector(uploadingController) { index ->
				scheduleUserSelectorCallback(uploadingController.getItem(index))
			}
		}
	}
	
	private fun placeScheduleUserSelector(
		uploadingController: RecyclerSelectorUploadingControllerBase,
		onClickCallback: (Int) -> Unit
	) {
		val fragment = bottomPopupContainer
		if (fragment != null) {
			bottomPopupContainer = fragment
			val container = fragment.binding.bottomPopupContainerContentContainer
			if (container.childCount > 2) {
				container.removeView(container.getChildAt(container.childCount - 1))
			}
			RecyclerSelectorPresenter(container, fragment, uploadingController, onClickCallback)
				.placeRecycler()
		}
	}
	
	//Other processing
	
	private fun throttleButtonBarButtonSelection() { selectionThrottleFlag = true }
	
	private fun unthrottleButtonBarButtonSelection() { selectionThrottleFlag = false }
	
	private fun getLessons(inputScheduleUser: RealmItemScheduleUser? = null) {
		removeErrorMessage()
		val mainObject = UserObserver.getMainObject()
		val university = mainObject.scheduleUniversity
		val scheduleUser = inputScheduleUser ?: mainObject.scheduleUser
		if (university == null || scheduleUser == null) router.openScheduleUserPicker()
		else {
			selectedWeekParity?.value = extensions.getCurrentWeekParity()
			Model(::onError).getLessons(university._id, scheduleUser._id) { lessons ->
				this.lessons = lessons
				placeSchedule(extensions.sortScheduleByParity(selectedWeekParity?.value, lessons))
			}
		}
	}
	
	private fun onError(errorType: ErrorType) {
		//TODO("Проверка ошибки, когда отправлен был неправильный _id с логгированием")
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
	
	private fun allowButtonBarClicks() { isButtonBarClickable = true }
	
	private fun restrictButtonBarClicks() { isButtonBarClickable = false }
	
	private fun scheduleUserSelectorCallback(scheduleUser: RealmItemScheduleUser?) {
		if (scheduleUser == null) {
			Log.e("ScheduleViewModel.scheduleUserSelectorCallback", "scheduleUser == null")
			makeErrorMessage(R.string.an_error_has_occurred_try_again)
		} else {
			getLessons(scheduleUser)
		}
		bottomPopupContainer?.animateExit()
	}
	
	private fun onScheduleUserSelectorError(errorType: ErrorType) {
		Log.e("LoginMethodSelectorViewModel.onSelectorError", errorType.toString())
		when (errorType) {
			ErrorType.ValidationError -> makeErrorMessage(R.string.server_error)
			ErrorType.UnknownServerError -> makeErrorMessage(R.string.server_error)
			ErrorType.NoInternetConnection -> Unit //This error will be displayed as toast
			else -> makeErrorMessage(R.string.an_error_has_occurred_try_again)
		}
		bottomPopupContainer?.animateExit()
	}
}