package com.q2ve.schedappv2.ui.buttonBar

import com.q2ve.schedappv2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

data class ButtonBarViewModel (
	val text: String,
	val selectionTextColor: Int = R.color.colorBlue,
	val selectionBackgroundColor: Int = R.color.colorBlueLight,
	val textColor: Int = R.color.colorLightGray1,
	val backgroundColor: Int = R.color.colorPlaqueBackground
)