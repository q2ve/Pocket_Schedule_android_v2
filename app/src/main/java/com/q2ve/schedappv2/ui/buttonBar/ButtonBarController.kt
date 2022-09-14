package com.q2ve.schedappv2.ui.buttonBar

import com.q2ve.schedappv2.helpers.Observable

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ButtonBarController(
	private val onItemSelectedCallback: (Int) -> Unit,
	viewModels: List<ButtonBarViewModel>,
	onStartSelection: Int?,
	onStartClickable: Boolean = true,
	private val allowRepeatedClicks: Boolean = false
) {
	private var clicksAllowed = onStartClickable
	var currentVisualSelection = Observable(onStartSelection)
	var viewModels = Observable(viewModels)
	
	fun setClickable(clickable: Boolean) { clicksAllowed = clickable }
	
	fun selectButton(index: Int) { currentVisualSelection.value = index }
	
	fun replaceButtons(viewModels: List<ButtonBarViewModel>) { this.viewModels.value = viewModels }
	
	fun onItemClicked(index: Int) {
		if (clicksAllowed && !(!allowRepeatedClicks && index == currentVisualSelection.value)) {
			currentVisualSelection.value = index
			onItemSelectedCallback(index)
		}
	}
}