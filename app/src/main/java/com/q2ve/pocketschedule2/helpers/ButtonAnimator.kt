package com.q2ve.pocketschedule2.helpers

import android.view.MotionEvent
import android.view.View

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Кнопка нажимается, даже если отвести палец. Пофикси, блять")
//TODO("Refactor it. Should be base onTouchListener with configurable actions.")
class ButtonAnimator(private val button: View) {
	
	fun animateWeakPressing() {
		val defaultNoAuthoriseScaleX = button.scaleX
		val defaultNoAuthoriseScaleY = button.scaleY
		
		button.setOnTouchListener { view, event ->
			fun setDefaultProperties(view: View) {
				view.scaleX = defaultNoAuthoriseScaleX
				view.scaleY = defaultNoAuthoriseScaleY
			}
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					view.scaleX *= 0.97f
					view.scaleY *= 0.97f
				}
				MotionEvent.ACTION_UP -> {
					setDefaultProperties(view)
					view.performClick()
				}
				MotionEvent.ACTION_CANCEL -> {
					setDefaultProperties(view)
				}
				MotionEvent.ACTION_OUTSIDE -> {
					setDefaultProperties(view)
				}
			}
			true
		}
	}
	
	fun animateStrongPressing() {
		val defaultNoAuthoriseScaleX = button.scaleX
		val defaultNoAuthoriseScaleY = button.scaleY
		
		button.setOnTouchListener { view, event ->
			fun setDefaultProperties(view: View) {
				view.scaleX = defaultNoAuthoriseScaleX
				view.scaleY = defaultNoAuthoriseScaleY
			}
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					view.scaleX *= 0.90f
					view.scaleY *= 0.90f
				}
				MotionEvent.ACTION_UP -> {
					setDefaultProperties(view)
					view.performClick()
				}
				MotionEvent.ACTION_CANCEL -> {
					setDefaultProperties(view)
				}
				MotionEvent.ACTION_OUTSIDE -> {
					setDefaultProperties(view)
				}
			}
			true
		}
	}
	
	fun animateStrongPressingWithFading() {
		val defaultNoAuthoriseScaleX = button.scaleX
		val defaultNoAuthoriseScaleY = button.scaleY
		val defaultNoAuthoriseAlpha = button.alpha
		
		button.setOnTouchListener { view, event ->
			fun setDefaultProperties(view: View) {
				view.scaleX = defaultNoAuthoriseScaleX
				view.scaleY = defaultNoAuthoriseScaleY
				view.alpha = defaultNoAuthoriseAlpha
			}
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					view.scaleX *= 0.90f
					view.scaleY *= 0.90f
					view.alpha -= 0.08f
				}
				MotionEvent.ACTION_UP -> {
					setDefaultProperties(view)
					view.performClick()
				}
				MotionEvent.ACTION_CANCEL -> {
					setDefaultProperties(view)
				}
				MotionEvent.ACTION_OUTSIDE -> {
					setDefaultProperties(view)
				}
			}
			true
		}
	}
}