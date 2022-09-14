package com.q2ve.schedappv2.ui.buttonBar

import android.content.res.Resources
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.databinding.ButtonBarBinding
import com.q2ve.schedappv2.helpers.ButtonAnimator

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Some effect when interaction is blocked.")
class ButtonBarView(private val controller: ButtonBarController) {
	private lateinit var resources: Resources
	private lateinit var theme: Resources.Theme
	private lateinit var buttonScroller: HorizontalScrollView
	private var buttons: MutableList<View> = emptyList<View>().toMutableList()
	
	fun createView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		resources: Resources,
		theme: Resources.Theme
	): View {
		this.resources = resources
		this.theme = theme
		
		val binding = ButtonBarBinding.inflate(inflater, container, false)
		val buttonBar = binding.buttonBarButtonsLayout
		buttonScroller = binding.buttonBarScroll
		
		inflateButtonBar(inflater, buttonBar, controller.viewModels.value)
		
		controller.viewModels.subscribe {
			inflateButtonBar(inflater, buttonBar, it)
		}
		
		controller.currentVisualSelection.subscribe {
			it?.let { if (it < buttons.size) selectButton(it) }
		}
		
		return binding.root
	}
	
	private fun inflateButtonBar(
		inflater: LayoutInflater,
		buttonBar: ViewGroup,
		viewModels: List<ButtonBarViewModel>
	) {
		buttons = emptyList<View>().toMutableList()
		viewModels.forEachIndexed { index, it ->
			val layout = R.layout.button_bar_item
			val button = inflater.inflate(layout, buttonBar, false)
			val buttonText = button.findViewById<TextView>(R.id.button_bar_item)
			buttonText.text = it.text
			buttonBar.addView(button)
			buttons += button
			ButtonAnimator(button).animateStrongPressingWithFading()
			button.setOnClickListener { controller.onItemClicked(index) }
		}
		uncolorizeAllButtons()
		controller.currentVisualSelection.value?.let { if (it < buttons.size) selectButton(it) }
	}
	
	private fun uncolorizeAllButtons() {
		buttons.forEachIndexed { index, it ->
			val button = it.findViewById<TextView>(R.id.button_bar_item)
			val textColor = resources.getColor(controller.viewModels.value[index].textColor, theme)
			val backgroundColor = resources.getColor(controller.viewModels.value[index].backgroundColor, theme)
			button.setTextColor(textColor)
			button.background.setTint(backgroundColor)
		}
	}
	
	private fun selectButton(buttonIndex: Int) {
		uncolorizeAllButtons()
		if (buttonIndex >= buttons.size) return
		val button = buttons[buttonIndex]
		val buttonText = button.findViewById<TextView>(R.id.button_bar_item)
		val textColor = resources.getColor(controller.viewModels.value[buttonIndex].selectionTextColor, theme)
		val backgroundColor = resources.getColor(controller.viewModels.value[buttonIndex].selectionBackgroundColor, theme)
		buttonText.setTextColor(textColor)
		button.background.setTint(backgroundColor)
		Handler().postDelayed(
			{ buttonScroller.smoothScrollTo(button.left - 400, 0) },
			30
		)
	}
}