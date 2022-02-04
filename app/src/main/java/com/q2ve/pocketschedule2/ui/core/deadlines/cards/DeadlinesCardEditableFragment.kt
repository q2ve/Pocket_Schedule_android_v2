package com.q2ve.pocketschedule2.ui.core.deadlines.cards
//
//import android.app.DatePickerDialog
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import java.text.SimpleDateFormat
//import java.util.*
//
///**
// * Created by Denis Shishkin on 28.08.2021.
// * qwq2eq@gmail.com
// */
//
////TODO("Правильное открытие клавиатуры, блять!")
////TODO("Setting subject.")
////TODO("Check updateButton shitty fix.")
////TODO("https://stackoverflow.com/questions/42367430/change-datepicker-header-text-color")
//class DeadlinesCardEditableFragment(
//	private val parent: DeadlinesCardEditableInterface,
//	private val deadline: RealmDeadlineObject,
//	private val createDeadline: Boolean = false
//): Fragment(), BottomMenuInterface, RecyclerPresenterInterface {
//	private val bottomMenuFragment = BottomMenuFragmentShitFix(ResourceGetter.getString("subject_selection"))
//	private val bottomMenuPresenter = BottomMenuPresenter(bottomMenuFragment, this)
//
//	override fun onCreateView(
//		inflater: LayoutInflater,
//		container: ViewGroup?,
//		savedInstanceState: Bundle?
//	): View? {
//		val rootView = inflater.inflate(R.layout.core_deadlines_card_editable, container, false)
//		val startTitle = deadline.title
//		val startDescription = deadline.description
//		var isDeadlineChanged = false
//
//		//title
//		val title = rootView.core_deadlines_card_editable_title
//		title.setOnFocusChangeListener { _, hasFocus ->
//			if (!hasFocus) { hideKeyboard() }
//		}
//		if (deadline.title == null ) { title.setText("") }
//		else { title.setText(deadline.title) }
//
//		//description
//		val description = rootView.core_deadlines_card_editable_description
//		if (deadline.description == null) { description.setText("") }
//		else { description.setText(deadline.description) }
//		description.setOnFocusChangeListener { _, hasFocus ->
//			if (!hasFocus) { hideKeyboard() }
//		}
//
//		//date
//		val dateFormat = SimpleDateFormat("dd MMMM yyyy")
//		val date = rootView.core_deadlines_card_editable_date
//		if (deadline.endDate == null) {
//			//date.text = "Время окончания не назначено"
//		} else {
//			val dateFormatted = dateFormat.format(deadline.endDate!!.toLong() * 1000)
//			date.text = dateFormatted
//		}
//		date.setOnClickListener {
//			val calendar = Calendar.getInstance()
//
//			var startSelectionIsToday = true
//			if (deadline.endDate != null) {
//				if (deadline.endDate!! > 1600000000) { startSelectionIsToday = false }
//			}
//
//			val startSelectionYear = if (startSelectionIsToday) {
//				calendar.get(Calendar.YEAR)
//			} else {
//				(SimpleDateFormat("yyyy").format(deadline.endDate!!.toLong() * 1000)).toInt()
//			}
//
//			val startSelectionMonth = if (startSelectionIsToday) {
//				calendar.get(Calendar.MONTH)
//			} else {
//				(SimpleDateFormat("MM").format(deadline.endDate!!.toLong() * 1000)).toInt() - 1
//			}
//
//			val startSelectionDay = if (startSelectionIsToday) {
//				calendar.get(Calendar.DAY_OF_MONTH)
//			} else {
//				(SimpleDateFormat("dd").format(deadline.endDate!!.toLong() * 1000)).toInt()
//			}
//
//			val datePicker = DatePickerDialog(
//				context!!,
//				{ _, selectedYear, monthOfYear, dayOfMonth ->
//					val rightMonthOfYear = monthOfYear + 1
//					val selection = ("$dayOfMonth.$rightMonthOfYear.$selectedYear")
//					val datePickerDate = SimpleDateFormat("dd.MM.yyyy").parse(selection)
//					if (datePickerDate != null) {
//						isDeadlineChanged = true
//						val selectedDate = datePickerDate.time / 1000
//						deadline.endDate = selectedDate.toInt()
//						val dateFormatted = dateFormat.format(datePickerDate.time)
//						date.text = dateFormatted
//					}
//				},
//				startSelectionYear,
//				startSelectionMonth,
//				startSelectionDay
//			)
//			if (createDeadline) {
//				datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
//			}
//			datePicker.show()
//		}
//
//		//subject
//		val subject = rootView.core_deadlines_card_editable_subject
//		if (deadline.subject == null) {
//			subject.text = ""
//		} else {
//			if (deadline.subject!!.name == null) {
//				subject.text = ""
//			} else {
//				subject.text = deadline.subject!!.name
//			}
//		}
//		subject.setOnClickListener {
//			bottomMenuFragment.presenter = bottomMenuPresenter
//			FragmentReplacer.addFragment(R.id.core_navigation_bottom_menu_frame, bottomMenuFragment)
//			RecyclerPresenter(this, R.id.bottom_menu_recycler_container_shit_fix, null, IndexerItemType.Subjects)
//		}
//
//		//updateButton
//		val updateButton = rootView.core_deadlines_card_editable_button_update
//		ButtonAnimator(updateButton)
//
//		fun checkRequiredParameters(): Boolean {
//			val markDate = view!!.core_deadlines_card_editable_date_required_mark
//			val markTitle = view!!.core_deadlines_card_editable_title_required_mark
//			var allow = true
//
//			//Log.e("endDate", deadline.endDate.toString())
//			if (deadline.endDate == null || deadline.endDate == 0) {
//				markDate.visibility = View.VISIBLE
//				allow = false
//			} else {
//				markDate.visibility = View.INVISIBLE
//			}
//
//			//Log.e("title", deadline.title.toString())
//			if (deadline.title == null || deadline.title == "") {
//				markTitle.visibility = View.VISIBLE
//				allow = false
//			} else {
//				markTitle.visibility = View.INVISIBLE
//			}
//
//			return allow
//		}
//
//		if (createDeadline) {
//			updateButton.text = resources.getText(R.string.create_deadline)
//			updateButton.setOnClickListener {
//				//Log.e("Update Click!", Calendar.getInstance().timeInMillis.toString())
//				deadline.title = title.text.toString()
//				deadline.description = description.text.toString()
//				if (checkRequiredParameters()) {
//					parent.postNewDeadline(deadline)
//				}
//			}
//		} else {
//			updateButton.setOnClickListener {
//				//Log.e("Update Click!", Calendar.getInstance().timeInMillis.toString())
//				deadline.title = title.text.toString()
//				deadline.description = description.text.toString()
//				if (checkRequiredParameters()) {
//					if (startDescription != deadline.description || startTitle != deadline.title) {
//						isDeadlineChanged = true
//					}
//					parent.updateDeadline(deadline, isDeadlineChanged)
//				}
//			}
//		}
//
//
//		//updateButtonShitFix
//		val updateButtonTwo = rootView.core_deadlines_card_editable_button_update2
//		updateButtonTwo.setOnClickListener {
//			Log.e("Update 2 Click!", Calendar.getInstance().timeInMillis.toString())
//			//I dunno why but it button doubleclicks. If remove it - updateButton will doubleclicks
//		}
//
//		return rootView
//	}
//
//	override fun bottomMenuClosed() {
//		FragmentReplacer.removeFragment(bottomMenuFragment)
//	}
//
//	override fun recyclerCallback(realmObject: RealmIdNameInterface, contentType: IndexerItemType) {
//		bottomMenuFragment.exitAnimation()
//		val subject = realmObject as RealmSubjectObject
//		val shitFix = "removeSubject"
//		if (subject._id == shitFix) {
//			deadline.subject = null
//		} else {
//			deadline.subject = subject
//		}
//
//		val subjectView = view?.core_deadlines_card_editable_subject
//		if (deadline.subject == null) {
//			subjectView?.text = ""
//		} else {
//			if (deadline.subject!!.name == null) {
//				subjectView?.text = ""
//			} else {
//				subjectView?.text = deadline.subject!!.name
//			}
//		}
//	}
//}