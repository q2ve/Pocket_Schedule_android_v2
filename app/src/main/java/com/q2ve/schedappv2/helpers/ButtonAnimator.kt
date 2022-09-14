package com.q2ve.schedappv2.helpers

import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.ViewCompat

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Кнопка нажимается, даже если отвести палец. Пофикси, блять")
//TODO("Refactor it. Should be base onTouchListener with configurable actions.")
class ButtonAnimator(private val button: View) {
	
	fun animateWeakPressing() { animate(0.97f, 0.97f) }
	
	fun animateStrongPressing() { animate(0.88f, 0.88f) }
	
	fun animateStrongPressingWithFading() { animate(0.88f, 0.88f, 0.08f) }
	
	private fun animate(scaleX: Float, scaleY: Float, alpha: Float = 0f) {
		val defaultScaleX = button.scaleX
		val defaultScaleY = button.scaleY
		val defaultAlpha = button.alpha
		
		button.setOnTouchListener { view, event ->
			fun setDefaultProperties(view: View) {
				ViewCompat.animate(view)
					.scaleX(defaultScaleX)
					.scaleY(defaultScaleY)
					.alpha(defaultAlpha)
					.setDuration(150)
					.setInterpolator(DecelerateInterpolator())
					.start()
			}
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					ViewCompat.animate(view)
						.scaleX(view.scaleX * scaleX)
						.scaleY(view.scaleY * scaleY)
						.alpha(view.alpha - alpha)
						.setDuration(100)
						.setInterpolator(DecelerateInterpolator())
						.withEndAction {
							ViewCompat.animate(view)
								.scaleX(view.scaleX + (defaultScaleX - view.scaleX) / 2.5f)
								.scaleY(view.scaleY + (defaultScaleY - view.scaleY) / 2.5f)
								.setDuration(100)
								.setInterpolator(AccelerateInterpolator())
								.start()
						}
						.start()
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