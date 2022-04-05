package com.q2ve.pocketschedule2.ui.core

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.q2ve.pocketschedule2.databinding.NavbarBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator

class NavbarController(private val defaultColor: Int, private val selectionColor: Int) {
	lateinit var binding: NavbarBinding
	
	fun buildNavbar(
		inflater: LayoutInflater,
		container: ViewGroup,
		onScheduleButtonPressed: () -> Unit,
		onDeadlinesButtonPressed: () -> Unit
	): View {
		binding = NavbarBinding.inflate(inflater, container, false  )
		
		var transitionsAllowed = true
		fun throttleTransition() {
			transitionsAllowed = false
			Handler().postDelayed({ transitionsAllowed = true }, 600)
		}
		
		val scheduleButton = binding.navbarScheduleButton
		ButtonAnimator(scheduleButton).animateStrongPressing()
		scheduleButton.setOnClickListener {
			if (transitionsAllowed) {
				throttleTransition()
				onScheduleButtonPressed()
			}
		}
		
		val deadlinesButton = binding.navbarDeadlinesButton
		ButtonAnimator(deadlinesButton).animateStrongPressing()
		deadlinesButton.setOnClickListener {
			if (transitionsAllowed) {
				throttleTransition()
				onDeadlinesButtonPressed()
			}
		}
		
		deselectAllButtons()
		
		return binding.root
	}
	
	fun scheduleSelected() {
		deselectAllButtons()
		binding.navbarScheduleButton.drawable.setTint(selectionColor)
	}
	
	fun deadlinesSelected() {
		deselectAllButtons()
		binding.navbarDeadlinesButton.drawable.setTint(selectionColor)
	}
	
	private fun deselectAllButtons() {
		fun deselect(button: ImageButton) {
			button.drawable.setTint(defaultColor)
		}
		deselect(binding.navbarScheduleButton)
		deselect(binding.navbarDeadlinesButton)
	}
}