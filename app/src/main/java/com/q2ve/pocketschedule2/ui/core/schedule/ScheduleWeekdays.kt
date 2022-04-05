package com.q2ve.pocketschedule2.ui.core.schedule

import com.q2ve.pocketschedule2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

enum class ScheduleWeekdays {
	Mon, Tue, Wed, Thu, Fri, Sat, None;

	fun getWeekdayName(): Int {
		return when (this) {
			Mon -> R.string.monday
			Tue -> R.string.tuesday
			Wed -> R.string.wednesday
			Thu -> R.string.thursday
			Fri -> R.string.friday
			Sat -> R.string.saturday
			None -> R.string.off_the_grid
		}
	}

	fun getShortWeekdayName(): Int {
		return when (this) {
			Mon -> R.string.mon
			Tue -> R.string.tue
			Wed -> R.string.wed
			Thu -> R.string.thu
			Fri -> R.string.fri
			Sat -> R.string.sat
			None -> R.string.off_the_grid
		}
	}
}