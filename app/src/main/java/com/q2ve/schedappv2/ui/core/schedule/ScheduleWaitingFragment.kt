package com.q2ve.schedappv2.ui.core.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.q2ve.schedappv2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleWaitingFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.schedule_waiting_dummy, container, false)
	}
}