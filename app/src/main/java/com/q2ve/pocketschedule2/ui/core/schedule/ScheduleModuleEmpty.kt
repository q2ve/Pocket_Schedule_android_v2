package com.q2ve.pocketschedule2.ui.core.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleModuleEmpty: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return inflater.inflate(R.layout.schedule_empty_day, container, false)
	}
}