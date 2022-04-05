package com.q2ve.pocketschedule2.ui.core.deadlines.cards

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import com.q2ve.pocketschedule2.model.ErrorType
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesCardController(private val sessionId: String) : DeadlinesCardControllerInterface {
	override fun openDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	) { Model(onError).openDeadline(sessionId, deadline._id, onCardResult) }
	
	override fun closeDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	) { Model(onError).closeDeadline(sessionId, deadline._id, onCardResult) }
	
	override fun deleteDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	) { Model(onError).deleteDeadline(sessionId, deadline._id) { onCardResult(null) } }
	
	override fun createDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	) { Model(onError).createDeadline(sessionId, deadline, onCardResult) }
	
	override fun updateDeadline(
		deadline: RealmItemDeadline,
		onError: (ErrorType) -> Unit,
		onCardResult: (RealmItemDeadline?) -> Unit
	) {
		Model(onError).putDeadline(sessionId, deadline, onCardResult)
	}
	
	override fun editDeadline(
		deadline: RealmItemDeadline,
		inflater: LayoutInflater,
		viewContainer: ViewGroup,
		resources: Resources,
		theme: Resources.Theme,
		closeCard: ((RealmItemDeadline?) -> Unit)?
	) {
		fun replaceViewInContainer(view: View) {
			ViewCompat.animate(viewContainer)
				.alpha(0f)
				.setInterpolator(AccelerateDecelerateInterpolator())
				.setDuration(200)
				.withEndAction {
					viewContainer.removeAllViews()
					viewContainer.addView(view)
					ViewCompat.animate(viewContainer)
						.alpha(1f)
						.setInterpolator(AccelerateDecelerateInterpolator())
						.setStartDelay(100)
						.setDuration(200)
						.start()
				}
				.start()
		}
		
		fun onEditionResult(deadline: RealmItemDeadline?) {
			if (deadline == null) closeCard?.invoke(null) else {
				val card = DeadlinesCard(sessionId, deadline, closeCard)
				val cardView = card.getView(inflater, viewContainer, resources, theme)
				replaceViewInContainer(cardView)
			}
			
		}
		
		val editableCardView = DeadlinesCardEditable(
			sessionId,
			false,
			::onEditionResult
		).getView(inflater, viewContainer, resources, deadline)
		replaceViewInContainer(editableCardView)
	}
}