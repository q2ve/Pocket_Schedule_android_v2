package com.q2ve.pocketschedule2.ui.core.deadlines

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.Observable
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadlineSource
import com.q2ve.pocketschedule2.ui.buttonBar.ButtonBarController
import com.q2ve.pocketschedule2.ui.buttonBar.ButtonBarView
import com.q2ve.pocketschedule2.ui.buttonBar.ButtonBarViewModel
import com.q2ve.pocketschedule2.ui.core.deadlines.pages.*
import com.q2ve.pocketschedule2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Send only changes parameters of deadline")
class DeadlinesViewModel: ViewModel(), DeadlinesViewModelInterface{
	private val router = DeadlinesRouter()
	private val extensions = DeadlinesViewModelExtensions()
	private var bottomPopupContainer: BottomPopupContainerFragment? = null
	private lateinit var buttonBarController: ButtonBarController
	
	private lateinit var sessionId: String
	private var selectionThrottleFlag = true
	private var isButtonBarClickable: Boolean = false
		set(isClickable) {
			if (this::buttonBarController.isInitialized) buttonBarController.setClickable(isClickable)
			field = isClickable
		}
	
	override var deadlinePages: Observable<List<DeadlinesPageBase>?>? = null
	override var selectedPage: Observable<Int>? = null
	override var errorMessage: Observable<Int?>? = null
	
	//TODO("Вью добавляется напрямую. А если фрагмент уже умер? Нужно сделать observable. И в других вьюмоделях аналогично.")
	override fun onCreateView(
		inflater: LayoutInflater,
		buttonBarContainer: ViewGroup,
		resources: Resources,
		theme: Resources.Theme
	) {
		throttleButtonBarButtonSelection()
		restrictButtonBarClicks()
		deadlinePages = Observable(null)
		selectedPage = Observable(0)
		errorMessage = Observable(null)
		
		val sessionId = UserObserver.getMainObject().sessionId
		if (sessionId == null) router.openAuthorizationRequirement() else {
			this.sessionId = sessionId
			Model(::onError).getDeadlineSources(sessionId) { externalSources ->
				val buttons: MutableList<ButtonBarViewModel> = emptyList<ButtonBarViewModel>().toMutableList()
				enumValues<DeadlinesType>().forEach {
					buttons += ButtonBarViewModel(resources.getString(it.getTypeName()))
				}
				externalSources.forEach {
					buttons += ButtonBarViewModel(it.name)
				}
				buttonBarController = ButtonBarController(
					::onButtonBarItemSelected,
					buttons,
					null,
					false
				)
				val buttonBar = ButtonBarView(buttonBarController).createView(
					inflater,
					buttonBarContainer,
					resources,
					theme
				)
				buttonBarContainer.addView(buttonBar)
				placeDeadlinePages(externalSources)
				allowButtonBarClicks()
			}
		}
	}
	
	override fun onViewCreated() {
		//TODO("Not yet implemented")
	}
	
	override fun onPagerPageSelected(position: Int) {
		selectButtonBarItem(position)
	}
	
	override fun onAddButtonPressed() {
		//TODO("Not yet implemented")
	}
	
	override fun onDestroyView() {
		deadlinePages = null
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
	
	private fun placeDeadlinePages(externalSources: List<RealmItemDeadlineSource>) {
		val pages: MutableList<DeadlinesPageBase> = emptyList<DeadlinesPageBase>().toMutableList()
		
		enumValues<DeadlinesType>().forEach {
			when (it) {
				DeadlinesType.Opened -> {
					pages.add(
						DeadlinesPageModuleOpened(
							R.layout.deadlines_module_empty_opened,
							sessionId,
							::onDeadlineClicked,
							::onDeadlineCheckboxClicked,
							::onError
						)
					)
				}
				DeadlinesType.Expired -> {
					pages.add(
						DeadlinesPageModuleExpired(
							R.layout.deadlines_module_empty_expired,
							sessionId,
							::onDeadlineClicked,
							::onDeadlineCheckboxClicked,
							::onError
						)
					)
				}
				DeadlinesType.Closed -> {
					pages.add(
						DeadlinesPageModuleClosed(
							R.layout.deadlines_module_empty_closed,
							sessionId,
							::onDeadlineClicked,
							::onDeadlineCheckboxClicked,
							::onError
						)
					)
				}
				else -> {
					Log.e(
						"DeadlinesViewModel.placeDeadlinePages",
						"Not implemented deadline type"
					)
					pages.add(DeadlinesPageLoadingDummy())
				}
			}
		}
		externalSources.forEach { source ->
			pages.add(
				DeadlinesPageModuleExternal(
					R.layout.deadlines_module_empty_external,
					sessionId,
					source._id,
					::onDeadlineClicked,
					::onDeadlineCheckboxClicked,
					::onError
				)
			)
		}
		deadlinePages?.value = pages
		allowButtonBarClicks()
	}
	
	private fun onDeadlineClicked(deadline: RealmItemDeadline) {
		//TODO("Not yet implemented")
	}
	
	private fun onDeadlineCheckboxClicked(deadline: RealmItemDeadline) {
		//TODO("Not yet implemented")
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
	
	private fun selectButtonBarItem(index: Int) {
		if (this::buttonBarController.isInitialized) buttonBarController.selectButton(index)
	}
	
	private fun selectPage(index: Int) { selectedPage?.value = index }
	
	private fun makeErrorMessage(stringId: Int) { errorMessage?.value = stringId }
	
	private fun removeErrorMessage() { errorMessage?.value = null }
	
	private fun throttleButtonBarButtonSelection() { selectionThrottleFlag = true }
	
	private fun unthrottleButtonBarButtonSelection() { selectionThrottleFlag = false }
	
	private fun allowButtonBarClicks() { isButtonBarClickable = true }
	
	private fun restrictButtonBarClicks() { isButtonBarClickable = false }
	
	//---------------------------------------------------------------------
	
	/*lateinit var deadlines: List<RealmItemDeadline>
	
	private val bottomMenuView = BottomMenuFragment(ResourceGetter.getString("deadline_card"))
	private val bottomMenuPresenter = BottomMenuPresenter(bottomMenuView, this)
	
	init {
		val mainObject = UserObserver.getMainObject()
		if (mainObject.currentUser == null || mainObject.sessionId == null) {
			authorizationRequired()
		} else {
			sessionId = mainObject.sessionId
		}
	}
	
	private fun authorizationRequired() {
		AuthorizationRequirement().show(showBackButton = false)
	}
	
	//Functions for deadlines main page are below
	
	private fun uploadDeadlines(escortingObject: DeadlinesEscortingObject) {
		if (sessionId == null) {
			authorizationRequired()
		} else {
			ContentGetter(this, escortingObject).getDeadlines(sessionId!!)
		}
	}
	
	private fun putDeadlines(
		inputDeadlines: List<RealmItemDeadline>,
		escortingObject: DeadlinesEscortingObject
	) {
		val emptyLayout = when {
			escortingObject.getOpened -> {
				ResourceGetter.getLayoutId("core_deadlines_module_empty_opened")
			}
			escortingObject.getExpired -> {
				ResourceGetter.getLayoutId("core_deadlines_module_empty_expired")
			}
			else -> {
				ResourceGetter.getLayoutId("core_deadlines_module_empty_closed")
			}
		}
		
		val outputDeadlines = filterDeadlines(inputDeadlines, escortingObject)
		val deadlinesModule = DeadlinesPageModuleBase(this, outputDeadlines, emptyLayout)
		val frameId = ResourceGetter.getId("core_deadlines_module_frame")
		
		if (fragment.checkView()) {
			FragmentReplacer.replaceFragment(frameId, deadlinesModule, ReplaceAnimation.Fading)
		}
		fragment.setButtonBarClickable(true)
	}
	
	private fun putDeadlineSources(inputDeadlines: List<RealmDeadlineSourceObject>) {
		fragment.putDeadlineSources(inputDeadlines)
	}
	
	override fun getDeadlines(
		deadlineSource: RealmDeadlineSourceObject?,
		needUploading: Boolean,
		getOpened: Boolean,
		getClosed: Boolean,
		getExpired: Boolean
	) {
		val parameters = DeadlinesEscortingObject(
			isItDeadlines = true,
			isItDeadlineSources = false,
			getExternal = deadlineSource,
			getOpened = getOpened,
			getClosed = getClosed,
			getExpired = getExpired
		)
		lastParameters = parameters
		
		if (needUploading) {
			uploadDeadlines(parameters)
		} else {
			putDeadlines(deadlines, parameters)
		}
	}

	override fun getDeadlineSources() {
//		val test1 = RealmDeadlineSourceObject("1488", "pro.guap")
//		val test2 = RealmDeadlineSourceObject("1483", "ifmo")
//		val test = listOf(test1, test2)
//		fragment.putDeadlineSources(test, isExpiredExist = true)
		
		if (sessionId == null) {
			authorizationRequired()
		} else {
			val parameters = DeadlinesEscortingObject(isItDeadlines = false, isItDeadlineSources = true)
			ContentGetter(this, parameters).getDeadlineSources(sessionId!!)
		}
	}
	
	//Functions for deadlines main page are above

	//Common functions are below
	
	override fun contentGetterCallback(
		objects: List<Any>?,
		isError: Boolean,
		errorType: ErrorType?,
		escortingObject: Any?
	) {
		if (isError) {
			when (errorType) {
				ErrorType.NoInternetConnection -> {
					//Do nothing. This error will be displayed as toast
				}
				ErrorType.UnknownServerError -> {
					PopupMessageSmall(isError = true, stringResource = com.q2ve.suai.R.string.validation_error)
				}
				else -> {
					PopupMessageSmall(isError = true, stringResource = R.string.validation_error)
				}
			}
		}
		
		val parameters = escortingObject as DeadlinesEscortingObject
		
		if (parameters.isItDeadlineSources) {
			val output = (objects as List<RealmDeadlineSourceObject>?) ?: emptyList()
			if (isError) {
				//TODO("SourcesGettingError")
			} else {
				putDeadlineSources(output)
			}
		}
		if (parameters.isItDeadlines) {
			val output = (objects as List<RealmItemDeadline>?) ?: emptyList()
			if (isError) {
				//TODO("DeadlinesGettingError")
			} else {
				deadlines = output
				putDeadlines(output, escortingObject)
			}
		}
		if (parameters.isItNewDeadline) {
			val output = (objects as List<RealmItemDeadline>?)
			if (isError || output == null) {
				//TODO("CreatingError")
			} else {
				addDeadline(output.first())
			}
		}
		if (parameters.isItEditedDeadline) {
			val output = (objects as List<RealmItemDeadline>?)
			if (isError || output == null) {
				//TODO("EditingError")
			} else {
				replaceUpdatedDeadline(output.first())
			}
		}
		if (parameters.isItDeletedDeadline) {
			val output = (objects as List<RealmItemDeadline>?)
			if (isError || output == null) {
				//TODO("DeletingError")
			} else {
				removeDeadline(output.first())
			}
		}
		
	}
	
	//Common functions are above
	
	//Functions for deadline cards are below
	
	private fun replaceUpdatedDeadline(deadline: RealmItemDeadline) {
		var updatedDeadlines: List<RealmItemDeadline> = emptyList()
		deadlines.forEach {
			if (it._id != deadline._id) { updatedDeadlines += it }
		}
		updatedDeadlines += deadline
		deadlines = updatedDeadlines
		
		putDeadlines(deadlines, lastParameters)
	}
	
	private fun addDeadline(deadline: RealmItemDeadline) {
		deadlines += deadline
		putDeadlines(deadlines, lastParameters)
	}
	
	private fun removeDeadline(deadline: RealmItemDeadline) {
		var updatedDeadlines: List<RealmItemDeadline> = emptyList()
		deadlines.forEach {
			if (it._id != deadline._id) { updatedDeadlines += it }
		}
		deadlines = updatedDeadlines
		
		putDeadlines(deadlines, lastParameters)
	}
	
	override fun getDeadlineCard(deadline: RealmItemDeadline, getBottomMenu: Boolean) {
		if (getBottomMenu) {
			bottomMenuView.presenter = bottomMenuPresenter
			FragmentReplacer.addFragment(R.id.core_navigation_bottom_menu_frame, bottomMenuView)
		}
		FragmentReplacer.replaceFragment(
			R.id.bottom_menu_recycler_container,
			DeadlinesCardFragment(this, deadline),
			ReplaceAnimation.Fading
		)
	}
	
	private fun getDeadlineCardEditable(
		deadline: RealmItemDeadline,
		createDeadline: Boolean = false
	) {
		bottomMenuView.presenter = bottomMenuPresenter
		FragmentReplacer.replaceFragment(
			R.id.bottom_menu_recycler_container,
			DeadlinesCardEditableFragment(this, deadline, createDeadline),
			ReplaceAnimation.Fading
		)
	}
	
	override fun bottomMenuClosed() {
		FragmentReplacer.removeFragment(bottomMenuView)
	}
	
	override fun closeDeadline(deadline: RealmItemDeadline, closeBottomMenu: Boolean) {
		fragment.setButtonBarClickable(false)
		fragment.putLoaderDummy()
		val parameters = DeadlinesEscortingObject(isItEditedDeadline = true)
		
		if (sessionId == null) {
			authorizationRequired()
		} else {
			ContentGetter(this, parameters).closeDeadline(sessionId!!, deadline)
		}
		
		if (closeBottomMenu) { bottomMenuView.exitAnimation() }
	}
	
	override fun openDeadline(deadline: RealmItemDeadline, closeBottomMenu: Boolean) {
		fragment.setButtonBarClickable(false)
		fragment.putLoaderDummy()
		val parameters = DeadlinesEscortingObject(isItEditedDeadline = true)
		
		if (sessionId == null) {
			authorizationRequired()
		} else {
			ContentGetter(this, parameters).openDeadline(sessionId!!, deadline)
		}
		
		if (closeBottomMenu) { bottomMenuView.exitAnimation() }
	}
	
	override fun editDeadline(deadline: RealmItemDeadline) {
		getDeadlineCardEditable(deadline)
	}
	
	override fun postNewDeadline(deadline: RealmItemDeadline) {
		bottomMenuView.exitAnimation()
		fragment.setButtonBarClickable(false)
		fragment.putLoaderDummy()
		val parameters = DeadlinesEscortingObject(isItNewDeadline = true)
		if (sessionId == null) {
			authorizationRequired()
		} else {
			ContentGetter(this, parameters).postNewDeadline(sessionId!!, deadline)
		}
	}
	
	override fun createDeadline() {
		bottomMenuView.presenter = bottomMenuPresenter
		FragmentReplacer.addFragment(R.id.core_navigation_bottom_menu_frame, bottomMenuView)
		getDeadlineCardEditable(RealmItemDeadline(), createDeadline = true)
	}
	
	override fun deleteDeadline(deadline: RealmItemDeadline) {
		bottomMenuView.exitAnimation()
		
		fragment.setButtonBarClickable(false)
		fragment.putLoaderDummy()
		val parameters = DeadlinesEscortingObject(isItDeletedDeadline = true)
		if (sessionId == null) {
			authorizationRequired()
		} else {
			ContentGetter(this, parameters).deleteDeadline(sessionId!!, deadline)
		}
	}
	
	override fun updateDeadline(deadline: RealmItemDeadline, isDeadlineChanged: Boolean) {
		getDeadlineCard(deadline, false)
		
		if (isDeadlineChanged) {
			fragment.setButtonBarClickable(false)
			fragment.putLoaderDummy()
			val parameters = DeadlinesEscortingObject(isItEditedDeadline = true)
			
			if (sessionId == null) {
				authorizationRequired()
			} else {
				ContentGetter(this, parameters).putDeadline(sessionId!!, deadline)
			}
		}
	}
	
	override fun onDeadlineItemClicked(realmObject: RealmItemDeadline) {
		Log.d("Deadlines", realmObject.title.toString())
		getDeadlineCard(realmObject)
	}
	
	override fun onDeadlineCheckboxClickedOLD(realmObject: RealmItemDeadline) {
		if (realmObject.isClosed) { closeDeadline(realmObject, false) }
		else { openDeadline(realmObject, false) }
	}
	
	//Functions for deadline cards are above*/
}