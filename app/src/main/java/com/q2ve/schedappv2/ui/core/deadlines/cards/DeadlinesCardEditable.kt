package com.q2ve.schedappv2.ui.core.deadlines.cards

import android.app.DatePickerDialog
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.databinding.DeadlinesCardEditableBinding
import com.q2ve.schedappv2.helpers.ButtonAnimator
import com.q2ve.schedappv2.helpers.Frames
import com.q2ve.schedappv2.helpers.UserObserver
import com.q2ve.schedappv2.helpers.hideKeyboard
import com.q2ve.schedappv2.helpers.navigator.Navigator
import com.q2ve.schedappv2.helpers.navigator.ReplaceAnimation
import com.q2ve.schedappv2.model.ErrorType
import com.q2ve.schedappv2.model.dataclasses.RealmItemDeadline
import com.q2ve.schedappv2.model.dataclasses.RealmItemSubject
import com.q2ve.schedappv2.ui.popup.BottomPopupContainerFragment
import com.q2ve.schedappv2.ui.popup.PopupMessagePresenter
import com.q2ve.schedappv2.ui.recyclerSelector.RecyclerSelectorPresenter
import com.q2ve.schedappv2.ui.recyclerSelector.RecyclerSelectorUploadingControllerBase
import com.q2ve.schedappv2.ui.recyclerSelector.RecyclerSelectorUploadingControllerSubjects
import com.q2ve.schedappv2.ui.recyclerSelector.SearchFieldPresenter
import com.q2ve.schedappv2.ui.scheduleUserPicker.ScheduleUserPickerFragment
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Правильное открытие клавиатуры, блять!")
//TODO("Check updateButton shitty fix.")
//TODO("https://stackoverflow.com/questions/42367430/change-datepicker-header-text-color")
class DeadlinesCardEditable(
	sessionId: String,
	private val isItNewDeadline: Boolean,
	private val closeCard: ((RealmItemDeadline?) -> Unit)?,
	private val controller: DeadlinesCardControllerInterface = DeadlinesCardController(sessionId)
) {
	lateinit var binding: DeadlinesCardEditableBinding
	
	private fun onCardResult(deadline: RealmItemDeadline) { closeCard?.invoke(deadline) }
	
	fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		resources: Resources,
		deadline: RealmItemDeadline
	): View {
		val binding = DeadlinesCardEditableBinding.inflate(inflater, container, false)
		this.binding = binding
		val startTitle = deadline.title
		val startDescription = deadline.description
		var isDeadlineChanged = false

		//title
		val title = binding.coreDeadlinesCardEditableTitle
		title.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) { container.context.hideKeyboard(View(container.context)) }
		}
		if (deadline.title == null ) { title.setText("") }
		else { title.setText(deadline.title) }

		//description
		val description = binding.coreDeadlinesCardEditableDescription
		if (deadline.description == null) { description.setText("") }
		else { description.setText(deadline.description) }
		description.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) { container.context.hideKeyboard(View(container.context)) }
		}

		//date
		val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
		val date = binding.coreDeadlinesCardEditableDate
		if (deadline.endDate == null) {
			//date.text = "Время окончания не назначено"
		} else {
			val dateFormatted = dateFormat.format(deadline.endDate!!.toLong() * 1000)
			date.text = dateFormatted
		}
		date.setOnClickListener {
			val calendar = Calendar.getInstance()

			var startSelectionIsToday = true
			if (deadline.endDate != null) {
				if (deadline.endDate!! > 1600000000) { startSelectionIsToday = false }
			}

			val startSelectionYear = if (startSelectionIsToday) {
				calendar.get(Calendar.YEAR)
			} else {
				(SimpleDateFormat("yyyy", Locale.US).format(deadline.endDate!!.toLong() * 1000)).toInt()
			}

			val startSelectionMonth = if (startSelectionIsToday) {
				calendar.get(Calendar.MONTH)
			} else {
				(SimpleDateFormat("MM", Locale.US).format(deadline.endDate!!.toLong() * 1000)).toInt() - 1
			}

			val startSelectionDay = if (startSelectionIsToday) {
				calendar.get(Calendar.DAY_OF_MONTH)
			} else {
				(SimpleDateFormat("dd", Locale.US).format(deadline.endDate!!.toLong() * 1000)).toInt()
			}

			val datePicker = DatePickerDialog(
				container.context,
				{ _, selectedYear, monthOfYear, dayOfMonth ->
					val rightMonthOfYear = monthOfYear + 1
					val selection = ("$dayOfMonth.$rightMonthOfYear.$selectedYear")
					val datePickerDate = SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(selection)
					if (datePickerDate != null) {
						isDeadlineChanged = true
						val selectedDate = datePickerDate.time / 1000
						deadline.endDate = selectedDate.toInt()
						val dateFormatted = dateFormat.format(datePickerDate.time)
						date.text = dateFormatted
					}
				},
				startSelectionYear,
				startSelectionMonth,
				startSelectionDay
			)
			if (isItNewDeadline) {
				datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
			}
			datePicker.show()
		}

		//subject
		val subject = binding.coreDeadlinesCardEditableSubject
		if (deadline.subject == null) {
			subject.text = ""
		} else {
			if (deadline.subject!!.name == null) {
				subject.text = ""
			} else {
				subject.text = deadline.subject!!.name
			}
		}
		subject.setOnClickListener {
			fun onSubjectSelectorCallback(subject: RealmItemSubject?) {
				deadline.subject = subject
				val subjectView = binding.coreDeadlinesCardEditableSubject
				if (subject?.name == null) subjectView.text = ""
				else subjectView.text = subject.name
			}
			
			fun onBottomMenuPlaced(fragment: BottomPopupContainerFragment) {
				val subjectSelectorContainer = fragment.binding.bottomPopupContainerContentContainer
				var selectorController: RecyclerSelectorUploadingControllerBase? = null
				
				//Placing Search Field
				subjectSelectorContainer.addView(View(container.context), 0, 40)
				fun searchItems(query: String) { selectorController?.searchItems(query) }
				SearchFieldPresenter().placeSearchField(
					subjectSelectorContainer,
					fragment.layoutInflater,
					::searchItems,
					fragment::hideKeyboard
				)
				
				//Placing Subject Selector
				val mainObject = UserObserver.getMainObject()
				val university = mainObject.scheduleUniversity
				val scheduleUser = mainObject.scheduleUser
				if (university == null || scheduleUser == null) {
					Frames.getActivityFrame()?.let { frame: Int ->
						Navigator.replaceFragment(
							ScheduleUserPickerFragment.newInstance(false),
							frame,
							ReplaceAnimation.SlideDownBounce,
							false
						)
						Navigator.clearBackstack()
					}
				} else {
					//TODO("Тестировать ::onError.")
					selectorController = RecyclerSelectorUploadingControllerSubjects(
						null,
						::onError,
						university._id,
						scheduleUser._id
					)
					RecyclerSelectorPresenter(
						subjectSelectorContainer,
						fragment,
						selectorController
					) { index ->
						fragment.animateExit()
						onSubjectSelectorCallback(selectorController.getItem(index))
					}.placeRecycler()
				}
			}
			
			val fragment = BottomPopupContainerFragment.newInstance(R.string.subject_choosing)
			val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
				if (event == Lifecycle.Event.ON_RESUME) {
					if (fragment.binding.bottomPopupContainerContentContainer.childCount == 0) {
						onBottomMenuPlaced(fragment)
					}
				}
			}
			fragment.lifecycle.addObserver(observer)
			Navigator.addFragment(fragment, Frames.getActivityFrame()!!, null, true)
		}

		//updateButton
		val updateButton = binding.coreDeadlinesCardEditableButtonUpdate
		ButtonAnimator(updateButton).animateWeakPressing()

		fun checkRequiredParameters(): Boolean {
			val markDate = binding.coreDeadlinesCardEditableDateRequiredMark
			val markTitle = binding.coreDeadlinesCardEditableTitleRequiredMark
			var allow = true

			//Log.e("endDate", deadline.endDate.toString())
			if (deadline.endDate == null || deadline.endDate == 0) {
				markDate.visibility = View.VISIBLE
				allow = false
			} else {
				markDate.visibility = View.INVISIBLE
			}

			//Log.e("title", deadline.title.toString())
			if (deadline.title == null || deadline.title == "") {
				markTitle.visibility = View.VISIBLE
				allow = false
			} else {
				markTitle.visibility = View.INVISIBLE
			}

			return allow
		}

		if (isItNewDeadline) {
			updateButton.text = resources.getText(R.string.create_deadline)
			updateButton.setOnClickListener {
				//Log.e("Update Click!", Calendar.getInstance().timeInMillis.toString())
				deadline.title = title.text.toString()
				deadline.description = description.text.toString()
				if (checkRequiredParameters()) {
					controller.createDeadline(deadline, ::onError, ::processOnCardResult)
				}
			}
		} else {
			updateButton.setOnClickListener {
				//Log.e("Update Click!", Calendar.getInstance().timeInMillis.toString())
				deadline.title = title.text.toString()
				deadline.description = description.text.toString()
				//TODO("WTF???")
				if (checkRequiredParameters()) {
					if (startDescription != deadline.description || startTitle != deadline.title) {
						onCardResult(deadline)
					}
					controller.updateDeadline(deadline, ::onError, ::processOnCardResult)
				}
			}
		}


		//updateButtonShitFix
		val updateButtonTwo = binding.coreDeadlinesCardEditableButtonUpdate2
		updateButtonTwo.setOnClickListener {
			Log.e("Update 2 Click!", Calendar.getInstance().timeInMillis.toString())
			//TODO("I dunno why but it button doubleclicks. If remove it - updateButton will doubleclicks.")
		}

		return binding.root
	}
	
	private fun processOnCardResult(deadline: RealmItemDeadline?) {
		if (deadline == null) onError(ErrorType.NullObject) else onCardResult(deadline)
	}
	
	fun onError(errorType: ErrorType) {
		//TODO("Better error displaying. E.g. splitting into types.")
		PopupMessagePresenter().createMessageSmall(
			true,
			R.string.an_error_has_occurred_try_again
		)
		closeCard?.let { it(null) }
	}
}