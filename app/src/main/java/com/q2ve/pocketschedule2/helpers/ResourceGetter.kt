package com.q2ve.pocketschedule2.helpers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

object ResourceGetter: Fragment() {
	private lateinit var activityLink: FragmentActivity
	
	fun configure(activity: FragmentActivity) {
		this.activityLink = activity
	}

	fun getColor(id: String): Int {
		val colorId = activityLink.resources.getIdentifier(id, "color", activityLink.packageName)
		return activityLink.resources.getColor(colorId)
	}
	
	fun getString(id: String): String {
		val stringId = activityLink.resources.getIdentifier(id, "string", activityLink.packageName)
		return activityLink.resources.getString(stringId)
	}
	
	fun getLayoutId(id: String): Int {
		return activityLink.resources.getIdentifier(id, "layout", activityLink.packageName)
	}
	
	fun getId(id: String): Int {
		return activityLink.resources.getIdentifier(id, "id", activityLink.packageName)
	}
}