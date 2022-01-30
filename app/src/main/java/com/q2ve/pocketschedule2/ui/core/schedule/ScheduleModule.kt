package com.q2ve.pocketschedule2.ui.core.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.helpers.ButtonAnimator


//TODO("Lesson card")
class ScheduleModule: Fragment() {
	companion object {
		fun newInstance(viewModels: Array<ScheduleLessonViewModel>?): ScheduleModule {
			val fragment = ScheduleModule()
			val arguments = Bundle()
			arguments.putParcelableArray("viewModels", viewModels)
			fragment.arguments = arguments
			return fragment
		}
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val lessons = requireArguments().getParcelableArray("viewModels")
		if (lessons == null) {
			return inflater.inflate(R.layout.schedule_empty_day, container, false)
		} else {
			val rootView = inflater.inflate(R.layout.schedule_module, container, false)
			val linearLayout = rootView.findViewById<LinearLayout>(R.id.schedule_module_layout)
			
			lessons.forEach {
				val lessonData = it as ScheduleLessonViewModel
				val layout = R.layout.schedule_lesson
				val lesson = layoutInflater.inflate(layout, linearLayout, false)
				
				val startTime = lesson.findViewById<TextView>(R.id.core_schedule_item_start_time)
				startTime.text = lessonData.startTime
				
				val endTime = lesson.findViewById<TextView>(R.id.core_schedule_item_end_time)
				endTime.text = lessonData.endTime
				
				val index = lesson.findViewById<TextView>(R.id.core_schedule_item_index)
				index.text = lessonData.lessonNum
				
				val subject = lesson.findViewById<TextView>(R.id.core_schedule_item_subject)
				subject.text = lessonData.subject
				
				val professor = lesson.findViewById<TextView>(R.id.core_schedule_item_professor)
				professor.text = lessonData.professors
				
				val tagBox = lesson.findViewById<FlexboxLayout>(R.id.core_schedule_item_tag_box)
				lessonData.tags.forEach { tag ->
					val tagLayout = R.layout.schedule_lesson_tag
					val tagView = layoutInflater.inflate(tagLayout, tagBox, false)
					val tagText = tagView.findViewById<TextView>(R.id.core_schedule_item_tag)
					tagText.text = tag
					tagBox.addView(tagView)
				}
				
				linearLayout.addView(lesson)
				ButtonAnimator(lesson).animateWeakPressing()
				lesson.setOnClickListener {
					//TODO("")
				}
			}
			return rootView
		}
	}
	
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
	}
	
	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
	}
}