package com.q2ve.pocketschedule2.ui.core.deadlines.cards

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.DeadlinesCardBinding
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import com.q2ve.pocketschedule2.ui.popup.PopupMessagePresenter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesCard(
	sessionId: String,
	private val deadline: RealmItemDeadline,
	private val closeCard: ((RealmItemDeadline?) -> Unit)? = null,
	private val controller: DeadlinesCardControllerInterface = DeadlinesCardController(sessionId)
) {
	private fun onCardResult(deadline: RealmItemDeadline?) { closeCard?.invoke(deadline) }
	
	fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		resources: Resources,
		theme: Resources.Theme
	): View {
		val binding = DeadlinesCardBinding.inflate(inflater, container, false)

		fun hideView(view: View) { view.visibility = View.GONE }

		//title
		val titleSign = binding.coreDeadlinesCardTitleSign
		val title = binding.coreDeadlinesCardTitle
		if (deadline.title == null || deadline.title == "") {
			hideView(titleSign)
			hideView(title)
		} else {
			title.text = deadline.title
		}

		//type
		val typeSign = binding.coreDeadlinesCardTypeSign
		val type = binding.coreDeadlinesCardType
		if (deadline.isExternal == false || deadline.type == null || deadline.type == "") {
			hideView(typeSign)
			hideView(type)
		} else {
			type.text = deadline.type
		}

		//description
		val descriptionSign = binding.coreDeadlinesCardDescriptionSign
		val description = binding.coreDeadlinesCardDescription
		if (deadline.description == null || deadline.description == "") {
			hideView(descriptionSign)
			hideView(description)
		} else {
			description.text = deadline.description
		}

		//date
		val date = binding.coreDeadlinesCardDate
		if (deadline.endDate == null) {
			date.text = "Время окончания не назначено"
		} else {
			val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
			val dateFormatted = dateFormat.format(deadline.endDate!!.toLong() * 1000)
			date.text = dateFormatted
		}

		//subject
		val subjectSign = binding.coreDeadlinesCardSubjectSign
		val subject = binding.coreDeadlinesCardSubject
		if (deadline.subject == null) {
			hideView(subjectSign)
			hideView(subject)
		} else {
			if (deadline.subject?.name == null) {
				hideView(subjectSign)
				hideView(subject)
			} else {
				subject.text = deadline.subject!!.name
			}
		}

		//points
		val pointsSign = binding.coreDeadlinesCardPointsSign
		val points = binding.coreDeadlinesCardPoints
		if (deadline.curPoints == null) {
			hideView(pointsSign)
			hideView(points)
		} else {
			points.text = deadline.curPoints.toString()
		}
		
		fun onError(errorType: ErrorType) {
			//TODO("Better error displaying. E.g. splitting into types.")
			PopupMessagePresenter().createMessageSmall(
				true,
				R.string.an_error_has_occurred_try_again
			)
			closeCard?.let { it(deadline) }
		}

		//closeButton
		val closeButton = binding.coreDeadlinesCardButtonClose
		if (deadline.isExternal == true || deadline.isExternal == null) hideView(closeButton) else {
			if (deadline.isClosed == true) {
				val icon = R.drawable.rc_deadline_card_button_open
				closeButton.background = ResourcesCompat.getDrawable(resources, icon, null)
				closeButton.text = resources.getString(R.string.open_deadline)
				closeButton.setTextColor(
					resources.getColor(R.color.colorBlue, theme)
				)
				closeButton.setOnClickListener {
					controller.openDeadline(deadline, ::onError, ::onCardResult)
				}
			} else {
				closeButton.setOnClickListener {
					controller.closeDeadline(deadline, ::onError, ::onCardResult)
				}
			}
		}

		//editButton
		val editButton = binding.coreDeadlinesCardButtonEdit
		if (deadline.isExternal == true || deadline.isExternal == null) {
			hideView(editButton)
		} else {
			editButton.setOnClickListener {
				controller.editDeadline(deadline, inflater, container, resources, theme, closeCard)
			}
		}

		//deleteButton
		val deleteButton = binding.coreDeadlinesCardButtonDelete
		var clicksCount = 0 //Эта хуйня даблкличит. TODO("Пофиксить даблклики блять")
		if (deadline.isExternal == true || deadline.isExternal == null) {
			hideView(deleteButton)
		} else {
			deleteButton.setOnClickListener {
				clicksCount ++
				if (clicksCount == 3) {
					//Log.d("SecondClick", Calendar.getInstance().timeInMillis.toString())
					deleteButton.background.setTintList(null)
					val deleteText = binding.coreDeadlinesCardButtonDeleteText
					deleteText.setText(R.string.delete)
					controller.deleteDeadline(deadline, ::onError, ::onCardResult)
				} else if (clicksCount == 1) {
					//Log.d("FirstClick", Calendar.getInstance().timeInMillis.toString())
					deleteButton.background.setTint(
						resources.getColor(R.color.colorRedMainLight, theme)
					)
					val deleteText = binding.coreDeadlinesCardButtonDeleteText
					deleteText.setText(R.string.delete_question)
				}
			}
		}

		return binding.root
	}
}