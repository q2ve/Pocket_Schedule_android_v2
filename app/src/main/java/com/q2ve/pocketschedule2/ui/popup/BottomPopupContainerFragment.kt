package com.q2ve.pocketschedule2.ui.popup

import android.animation.LayoutTransition
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.q2ve.pocketschedule2.databinding.BottomPopupContainerBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator
import com.q2ve.pocketschedule2.helpers.navigator.Navigator

class BottomPopupContainerFragment: Fragment() {
	companion object {
		fun newInstance(titleId: Int, isFullScreen: Boolean = false): BottomPopupContainerFragment {
			val fragment = BottomPopupContainerFragment()
			fragment.arguments = bundleOf(
				"titleId" to titleId,
				"isFullScreen" to isFullScreen
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
		
		val menu = binding.bottomPopupContainerContainer
		val bottomSheetBehavior = BottomSheetBehavior.from(menu)
		bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
			override fun onStateChanged(bottomSheet: View, newState: Int) {
				if (newState == BottomSheetBehavior.STATE_COLLAPSED) animateExit()
			}
			override fun onSlide(bottomSheet: View, slideOffset: Float) { }
		})
		Handler(Looper.getMainLooper()).postDelayed(
			{ bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }, 10
		)
		
		val background: LinearLayout = binding.bottomPopupContainerBackground
		background.setOnClickListener{
			if (isMenuFullyOpened) {
				animateExit()
			}
			
		}
		
		val exitButton: Button = binding.bottomPopupContainerExitButton
		ButtonAnimator(exitButton).animateStrongPressing()
		exitButton.setOnClickListener{
			if (isMenuFullyOpened) {
				animateExit()
			}
		}
		
		bottomMenuTitle.text = arguments?.getInt("titleId")?.let { getString(it) }
		//Log.e("TEST", TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics).toString())
		background.alpha = 0f
		background.translationY = 0f
		//TODO("Replace with startDelay.")
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
		
		//val bottomMenuContainer = binding.bottomPopupContainerContainer
		//bottomMenuContainer.setOnClickListener{ hideKeyboard() } так не работает свайпание
		
		val isFullScreen = arguments?.getBoolean("isFullScreen") ?: false
		if (isFullScreen) {
			binding.bottomPopupContainerMotionLayout.layoutParams
				.height = ViewGroup.LayoutParams.MATCH_PARENT
			binding.bottomPopupContainerContainer.layoutParams
				.height = LinearLayout.LayoutParams.MATCH_PARENT
			binding.bottomPopupContainerContentContainer.layoutParams
				.height = LinearLayout.LayoutParams.MATCH_PARENT
		}
		
		//EnableTransitionType is necessary to xml "animateLayoutChanges" works with height changing
		val motionLayout = binding.bottomPopupContainerMotionLayout
		motionLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
		
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
	
	/**
	 * OnResume callback will not be work if you'll add view in
	 * bottom_popup_container_content_container before onResume.
	 */
	fun buildObserver(onResume: ((BottomPopupContainerFragment) -> Unit)? = null) {
		val observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
			if (event == Lifecycle.Event.ON_RESUME) onResume?.let {
				if (this.binding.bottomPopupContainerContentContainer.childCount == 0) it(this)
			}
		}
		this.lifecycle.addObserver(observer)
	}
}