package com.q2ve.schedappv2.ui.core.deadlines

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.helpers.Observable
import com.q2ve.schedappv2.helpers.UserObserver
import com.q2ve.schedappv2.model.ErrorType
import com.q2ve.schedappv2.model.Model
import com.q2ve.schedappv2.model.dataclasses.RealmItemDeadline
import com.q2ve.schedappv2.model.dataclasses.RealmItemDeadlineSource
import com.q2ve.schedappv2.ui.buttonBar.ButtonBarController
import com.q2ve.schedappv2.ui.buttonBar.ButtonBarView
import com.q2ve.schedappv2.ui.buttonBar.ButtonBarViewModel
import com.q2ve.schedappv2.ui.core.deadlines.cards.DeadlinesCardEditable
import com.q2ve.schedappv2.ui.core.deadlines.pages.*
import com.q2ve.schedappv2.ui.popup.BottomPopupContainerFragment

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Send only changes parameters of deadline")
class DeadlinesViewModel: ViewModel(), DeadlinesViewModelInterface{
	private val router = DeadlinesRouter()
	private var bottomPopupContainer: BottomPopupContainerFragment? = null
	private lateinit var buttonBarController: ButtonBarController
	private lateinit var shitFix: DeadlinesPageModuleOpened
	
	private lateinit var sessionId: String
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
					buttons += when (it) {
						DeadlinesType.Opened -> {
							ButtonBarViewModel(resources.getString(it.getTypeName()))
						}
						DeadlinesType.Expired -> {
							ButtonBarViewModel(
								resources.getString(it.getTypeName()),
								R.color.colorDeadlineRed,
								R.color.colorRedMainLight
							)
						}
						DeadlinesType.Closed -> {
							ButtonBarViewModel(
								resources.getString(it.getTypeName()),
								R.color.colorDeadlineGreen,
								R.color.colorGreenLight
							)
						}
						else -> {
							ButtonBarViewModel(resources.getString(it.getTypeName()))
						}
					}
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
		router.openBottomPopupContainer(R.string.deadline_card) {
			bottomPopupContainer = it
			val card = DeadlinesCardEditable(
				sessionId,
				true,
				{ bottomPopupContainer?.animateExit() }
			)
			val container = it.binding.bottomPopupContainerContentContainer
			val view = card.getView(it.layoutInflater, container, it.resources, RealmItemDeadline())
			container.addView(view)
		}
	}
	
	override fun onDestroyView() {
		deadlinePages?.value?.forEach { it.onDestroyView() }
		deadlinePages = null
		selectedPage = null
		errorMessage = null
	}
	
	private fun onButtonBarItemSelected(selectedItem: Int) {
		if (isButtonBarClickable) {
			removeErrorMessage()
			selectPage(selectedItem)
		}
	}
	
	private fun placeDeadlinePages(externalSources: List<RealmItemDeadlineSource>) {
		val pages: MutableList<DeadlinesPageBase> = emptyList<DeadlinesPageBase>().toMutableList()
		
		enumValues<DeadlinesType>().forEach {
			when (it) {
				DeadlinesType.Opened -> {
					shitFix = DeadlinesPageModuleOpened(
						R.layout.deadlines_module_empty_opened,
						sessionId,
						::onError
					)
					pages.add(
						shitFix
					)
				}
				DeadlinesType.Expired -> {
					pages.add(
						DeadlinesPageModuleExpired(
							R.layout.deadlines_module_empty_expired,
							sessionId,
							::onError
						)
					)
				}
				DeadlinesType.Closed -> {
					pages.add(
						DeadlinesPageModuleClosed(
							R.layout.deadlines_module_empty_closed,
							sessionId,
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
					::onError
				)
			)
		}
		deadlinePages?.value = pages
		allowButtonBarClicks()
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
	
	private fun allowButtonBarClicks() { isButtonBarClickable = true }
	
	private fun restrictButtonBarClicks() { isButtonBarClickable = false }
}