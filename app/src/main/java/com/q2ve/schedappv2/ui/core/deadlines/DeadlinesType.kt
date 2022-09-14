package com.q2ve.schedappv2.ui.core.deadlines

import com.q2ve.schedappv2.R

enum class DeadlinesType {
	Opened, Expired, Closed;
	
	fun getTypeName(): Int {
		return when (this) {
			Opened -> R.string.opened
			Expired -> R.string.expired
			Closed -> R.string.closed
		}
	}
}