package com.q2ve.pocketschedule2.helpers

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Заменить на проброс айдишников через аргументы")
object Frames {
	private var activityFrame: Int? = null
	private var loginFrame: Int? = null
	private var coreFrame: Int? = null
	
	fun registerActivityFrame(id: Int) { activityFrame = id }
	fun registerLoginFrame(id: Int) { loginFrame = id }
	fun registerCoreFrame(id: Int) { coreFrame = id }
	
	fun getActivityFrame() = activityFrame
	fun getLoginFrame() = loginFrame
	fun getCoreFrame() = coreFrame
	
	fun unregisterActivityFrame() { activityFrame = null }
	fun unregisterLoginFrame() { loginFrame = null }
	fun unregisterCoreFrame() { coreFrame = null }
}