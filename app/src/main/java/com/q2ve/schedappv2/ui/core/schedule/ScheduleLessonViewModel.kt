package com.q2ve.schedappv2.ui.core.schedule

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

@Parcelize
data class ScheduleLessonViewModel (
	val _id: String,
	val startTime: String? = null,
	val endTime: String? = null,
	val lessonNum: String? = null,
	val subject: String? = null,
	val professors: String? = null,
	val tags: List<String> = emptyList()
): Parcelable
