package com.q2ve.pocketschedule2.ui.core.schedule

import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemLesson
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ScheduleViewModelExtensions {
	fun getCurrentWeekParity(): ScheduleWeekParity {
		val currentTime = System.currentTimeMillis() / 1000
		val currentWeek = SimpleDateFormat("w", Locale.US).format(currentTime).toInt()
		return if (currentWeek % 2 == 0) ScheduleWeekParity.Even else ScheduleWeekParity.Odd
	}
	
	fun getCurrentWeekday (): ScheduleWeekdays {
		return when (SimpleDateFormat("EEE", Locale.US).format(Date())) {
			"Mon" -> ScheduleWeekdays.Mon
			"Tue" -> ScheduleWeekdays.Tue
			"Wed" -> ScheduleWeekdays.Wed
			"Thu" -> ScheduleWeekdays.Thu
			"Fri" -> ScheduleWeekdays.Fri
			"Sat" -> ScheduleWeekdays.Sat
			else -> ScheduleWeekdays.None
		}
	}
	
	fun sortScheduleByParity(
		selectedWeekParity: ScheduleWeekParity?,
		objects: List/*TODO ARRAY*/<RealmItemLesson>
	): Array<RealmItemLesson> {
		val weekParity = when (selectedWeekParity) {
			ScheduleWeekParity.Odd -> "odd"
			ScheduleWeekParity.Even -> "even"
			null -> "odd"
		}
		var output: Array<RealmItemLesson> = emptyArray()
		objects.forEach { item ->
			if (item.week == weekParity || item.week == "none") output += item
		}
		output.sortBy { it.lessonNum }
		return output
	}
	
	fun inflateModuleFragments(schedule: Array<RealmItemLesson>): Array<Fragment> {
		var output: Array<Fragment> = emptyArray()
		
		enumValues<ScheduleWeekdays>().forEach { weekday ->
			var lessonsOfThisWeekday: Array<RealmItemLesson> = emptyArray()
			val dayOfTheWeek = weekday.toString().lowercase()
			
			schedule.forEach { lesson ->
				if (lesson.day == dayOfTheWeek) {
					lessonsOfThisWeekday += lesson
				} else if (weekday == ScheduleWeekdays.None && (lesson.day == null || lesson.day == "")) {
					lessonsOfThisWeekday += lesson
				}
			}
			if (lessonsOfThisWeekday.isEmpty()) {
				output += ScheduleModuleEmpty()
			} else {
				lessonsOfThisWeekday.sortBy { it.lessonNum }
				val viewModels = convertLessons(lessonsOfThisWeekday)
				val fragment = ScheduleModule.newInstance(viewModels)
				output += fragment
			}
		}
		
		return output
	}
	
	private fun convertLessons(input: Array<RealmItemLesson>): Array<ScheduleLessonViewModel> {
		fun lessonType(input: String?): String {
			return when (input) {
				"lecture" -> { "Лекция" }
				"lab" -> { "Лабораторная работа" }
				"practice" -> { "Практика" }
				"test" -> { "Тест" }
				"course" -> { "Курс" }
				else -> { "Нетипированная" }
			}
		}
		fun pluralizeGroups(count: Int): String {
			return when {
				(count < 1) -> { "Группы не назначены" }
				(count == 1) -> { "$count группа" }
				(count in 2..4) -> { "$count группы" }
				(count in 5..20) -> { "$count групп" }
				else -> { "Что-то дофига групп" }
			}
		}
		
		var output: Array<ScheduleLessonViewModel> = emptyArray()
		input.forEach {
			//professors
			var professors = ""
			if (it.professors != null) {
				it.professors!!.forEach { professor ->
					val professorName = professor.name
					if (professors == "") professors = professorName
					else professors += ", $professorName"
				}
			} else professors = "Без преподавателя"
			
			//tags
			val tags = emptyList<String>().toMutableList()
			if (it.rooms != null && it.rooms != "null; null") {
				tags += it.rooms!!
			}
			tags += lessonType(it.type)
			if (it.groups!!.size != 1) { tags += pluralizeGroups(it.groups!!.size) }
			if (it.startTime == null || it.endTime == null) { tags += "Время не назначено" }
			
			//startTime
			val startTime = when {
				(it.startTime == null) -> { "xx:xx" }
				(it.startTime!!.length < 5) -> { "0" + it.startTime }
				else -> { it.startTime }
			}
			
			//endTime
			val endTime = when {
				(it.endTime == null) -> { "xx:xx" }
				(it.endTime!!.length < 5) -> { "0" + it.endTime }
				else -> { it.endTime }
			}
			
			//lessonNum
			val lessonNum = it.lessonNum ?: ""
			
			//subject
			val subject = when {
				it.subject == null -> { "Без предмета" }
				it.subject!!.name == null -> { "Без предмета" }
				else -> {
					it.subject!!.name
				}
			}
			
			val item = ScheduleLessonViewModel(
				it._id,
				startTime,
				endTime,
				lessonNum,
				subject,
				professors,
				tags
			)
			
			output += item
		}
		return output
	}
}