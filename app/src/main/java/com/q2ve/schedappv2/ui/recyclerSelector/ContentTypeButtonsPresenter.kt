package com.q2ve.schedappv2.ui.recyclerSelector

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.q2ve.schedappv2.databinding.RecyclerContentTypeButtonsBinding
import com.q2ve.schedappv2.helpers.ButtonAnimator

class ContentTypeButtonsPresenter {
	private var interactionThrottled = false
	
	fun placeButtons(
		container: ViewGroup,
		inflater: LayoutInflater,
		textColor: Int,
		backgroundColor: Int,
		selectionTextColor: Int,
		selectionBackgroundColor: Int,
		onGroupsSelectedCallback: () -> Unit,
		onProfessorsSelectedCallback: () -> Unit
	) {
		val buttons = RecyclerContentTypeButtonsBinding.inflate(
			inflater,
			container,
			false
		)
		val groupsButton = buttons.recyclerViewSelectorGroupsButton
		ButtonAnimator(groupsButton).animateStrongPressing()
		val professorsButton = buttons.recyclerViewSelectorProfessorsButton
		ButtonAnimator(professorsButton).animateStrongPressing()
		
		fun uncolorizeButtons() {
			groupsButton.setTextColor(textColor)
			groupsButton.background.setTint(backgroundColor)
			professorsButton.setTextColor(textColor)
			professorsButton.background.setTint(backgroundColor)
		}
		fun colorizeGroupsButton() {
			uncolorizeButtons()
			groupsButton.setTextColor(selectionTextColor)
			groupsButton.background.setTint(selectionBackgroundColor)
		}
		fun colorizeProfessorsButton() {
			uncolorizeButtons()
			professorsButton.setTextColor(selectionTextColor)
			professorsButton.background.setTint(selectionBackgroundColor)
		}
		
		groupsButton.setOnClickListener {
			if (!interactionThrottled) {
				throttleInteraction()
				onGroupsSelectedCallback()
				colorizeGroupsButton()
			}
		}
		professorsButton.setOnClickListener {
			if (!interactionThrottled) {
				throttleInteraction()
				onProfessorsSelectedCallback()
				colorizeProfessorsButton()
			}
		}
		
		colorizeGroupsButton()
		container.addView(buttons.root)
	}
	
	private fun throttleInteraction() {
		interactionThrottled = true
		Handler().postDelayed({ interactionThrottled = false }, 500)
	}
}