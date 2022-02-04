package com.q2ve.pocketschedule2.ui.core.deadlines.cards
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.res.ResourcesCompat
//import androidx.fragment.app.Fragment
//import com.q2ve.suai.R
//import com.q2ve.suai.helpers.realm.objects.RealmDeadlineObject
//import kotlinx.android.synthetic.main.core_deadlines_card.view.*
//import java.text.SimpleDateFormat
//
///**
// * Created by Denis Shishkin on 28.08.2021.
// * qwq2eq@gmail.com
// */
//
////TODO("Setting time.")
//class DeadlinesCardFragment(
//	private val parent: DeadlinesCardInterface,
//	private val deadline: RealmDeadlineObject
//): Fragment() {
//	override fun onCreateView(
//		inflater: LayoutInflater,
//		container: ViewGroup?,
//		savedInstanceState: Bundle?
//	): View? {
//		val rootView = inflater.inflate(R.layout.core_deadlines_card, container, false)
//
//		fun hideView(view: View) { view.visibility = View.GONE }
//
//		//title
//		val titleSign = rootView.core_deadlines_card_title_sign
//		val title = rootView.core_deadlines_card_title
//		if (deadline.title == null || deadline.title == "") {
//			hideView(titleSign)
//			hideView(title)
//		} else {
//			title.text = deadline.title
//		}
//
//		//type
//		val typeSign = rootView.core_deadlines_card_type_sign
//		val type = rootView.core_deadlines_card_type
//		if (!deadline.isExternal || deadline.type == null || deadline.type == "") {
//			hideView(typeSign)
//			hideView(type)
//		} else {
//			type.text = deadline.type
//		}
//
//		//description
//		val descriptionSign = rootView.core_deadlines_card_description_sign
//		val description = rootView.core_deadlines_card_description
//		if (deadline.description == null || deadline.description == "") {
//			hideView(descriptionSign)
//			hideView(description)
//		} else {
//			description.text = deadline.description
//		}
//
//		//date
//		val dateSign = rootView.core_deadlines_card_date_sign
//		val date = rootView.core_deadlines_card_date
//		if (deadline.endDate == null) {
//			date.text = "Время окончания не назначено"
//		} else {
//			val dateFormat = SimpleDateFormat("dd MMMM yyyy")
//			val dateFormatted = dateFormat.format(deadline.endDate!!.toLong() * 1000)
//			date.text = dateFormatted
//		}
//
//		//subject
//		val subjectSign = rootView.core_deadlines_card_subject_sign
//		val subject = rootView.core_deadlines_card_subject
//		if (deadline.subject == null) {
//			hideView(subjectSign)
//			hideView(subject)
//		} else {
//			if (deadline.subject!!.name == null) {
//				hideView(subjectSign)
//				hideView(subject)
//			} else {
//				subject.text = deadline.subject!!.name
//			}
//		}
//
//		//points
//		val pointsSign = rootView.core_deadlines_card_points_sign
//		val points = rootView.core_deadlines_card_points
//		if (deadline.curPoints == null) {
//			hideView(pointsSign)
//			hideView(points)
//		} else {
//			points.text = deadline.curPoints.toString()
//		}
//
//		//closeButton
//		val closeButton = rootView.core_deadlines_card_button_close
//		if (deadline.isExternal) {
//			hideView(closeButton)
//		} else {
//			if (deadline.isClosed) {
//				val icon = R.drawable.rc_deadline_card_button_open
//				closeButton.background = ResourcesCompat.getDrawable(resources, icon, null)
//				closeButton.text = resources.getString(R.string.open_deadline)
//				closeButton.setTextColor(resources.getColor(R.color.colorBlue))
//				closeButton.setOnClickListener { parent.openDeadline(deadline) }
//			} else {
//				closeButton.setOnClickListener { parent.closeDeadline(deadline) }
//			}
//		}
//
//		//editButton
//		val editButton = rootView.core_deadlines_card_button_edit
//		if (deadline.isExternal) {
//			hideView(editButton)
//		} else {
//			editButton.setOnClickListener { parent.editDeadline(deadline) }
//		}
//
//		//deleteButton
//		val deleteButton = rootView.core_deadlines_card_button_delete
//		var clicksCount = 0 //Эта хуйня даблкличит. TODO("Пофиксить даблклики блять")
//		if (deadline.isExternal) {
//			hideView(deleteButton)
//		} else {
//			deleteButton.setOnClickListener {
//				clicksCount ++
//				if (clicksCount == 3) {
//					//Log.d("SecondClick", Calendar.getInstance().timeInMillis.toString())
//					deleteButton.background.setTintList(null)
//					val deleteText = deleteButton.core_deadlines_card_button_delete_text
//					deleteText.setText(R.string.delete)
//					parent.deleteDeadline(deadline)
//				} else if (clicksCount == 1) {
//					//Log.d("FirstClick", Calendar.getInstance().timeInMillis.toString())
//					deleteButton.background.setTint(resources.getColor(R.color.colorRedMainLight))
//					val deleteText = deleteButton.core_deadlines_card_button_delete_text
//					deleteText.setText(R.string.delete_question)
//				}
//			}
//		}
//
//		return rootView
//	}
//}