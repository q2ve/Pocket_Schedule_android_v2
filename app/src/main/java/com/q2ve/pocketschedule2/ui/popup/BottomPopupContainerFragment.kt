package com.q2ve.pocketschedule2.ui.popup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.databinding.BottomPopupContainerBinding
import com.q2ve.pocketschedule2.helpers.navigator.Navigator


//TODO("Smooth height increasing")
class BottomPopupContainerFragment: Fragment() {
	companion object {
		fun newInstance(titleId: Int): BottomPopupContainerFragment {
			val fragment = BottomPopupContainerFragment()
			fragment.arguments = bundleOf(
				"titleId" to titleId
			)
			return fragment
		}
	}
	
	lateinit var binding: BottomPopupContainerBinding
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = BottomPopupContainerBinding.inflate(inflater, container, false)
		
		var isMenuFullyOpened = false
		
		val bottomMenuTitle: TextView = binding.bottomPopupContainerTitle
		
		val menu: MotionLayout = binding.bottomPopupContainerMotionLayout
		menu.setTransitionListener(
			object: MotionLayout.TransitionListener {
				override fun onTransitionStarted(
					motionLayout: MotionLayout?,
					startId: Int,
					endId: Int
				) {}
				
				override fun onTransitionChange(
					motionLayout: MotionLayout?,
					startId: Int,
					endId: Int,
					progress: Float
				) {
					Log.d("Motion", progress.toString())
					if (progress > 0.9) {
						animateExit()
					}
				}
				
				override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}
				
				override fun onTransitionTrigger(
					motionLayout: MotionLayout?,
					triggerId: Int,
					positive: Boolean,
					progress: Float
				) {}
			}
		)
		
		val background: LinearLayout = binding.bottomPopupContainerBackground
		background.setOnClickListener{
			if (isMenuFullyOpened) {
				animateExit()
			}
			
		}
		
		val exitButton: Button = binding.bottomPopupContainerExitButton
		exitButton.setOnClickListener{
			if (isMenuFullyOpened) {
				animateExit()
			}
		}
		
		bottomMenuTitle.text = arguments?.getInt("titleId")?.let { getString(it) }
		
		background.alpha = 0f
		background.translationY = 0f
		ViewCompat.animate(background) //Anti-lag shit, does nothing but delay
			.alpha(0.0f)
			.setDuration(120)
			.withEndAction {
				ViewCompat.animate(background)
					.alpha(0.7f)
					.setDuration(400)
					.start()
				ViewCompat.animate(menu)
					.translationY(0f)
					.setDuration(600)
					.setInterpolator(DecelerateInterpolator())
					.withEndAction{ isMenuFullyOpened = true }
					.start()
			}
			.start()
		
		val bottomMenuContainer = binding.bottomPopupContainerContainer
		//bottomMenuContainer.setOnClickListener{ hideKeyboard() } так не работает свайпание
		
		return binding.root
	}
	
	fun animateExit() {
		val background: LinearLayout = binding.bottomPopupContainerBackground
		val menu: LinearLayout = binding.bottomPopupContainerContainer
		
		background.alpha = 0.7f
		ViewCompat.animate(background)
			.alpha(0f)
			.setDuration(300)
			.setInterpolator(AccelerateInterpolator())
			.start()
		ViewCompat.animate(menu)
			.translationY(3000f)
			.setDuration(300)
			.setInterpolator(AccelerateInterpolator())
			.withEndAction {
				background.translationY = 3000f
				Navigator.removeFragment(this)
			}
			.start()
	}
}