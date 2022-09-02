package com.q2ve.pocketschedule2.ui.bottomSheet

import android.animation.LayoutTransition
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.q2ve.pocketschedule2.databinding.BottomSheetBinding

class BottomSheetView: BottomSheetContract.View, FrameLayout {
	
	//Constructors
	
	constructor(
		context: Context
	): super(context, null) { initView() }
	
	constructor(
		context: Context,
		attributes: AttributeSet
	): super(context, attributes) { initView() }
	
	constructor(
		context: Context,
		attributes: AttributeSet,
		defaultStyle: Int
	): super(context, attributes, defaultStyle) { initView() }
	
	//.Constructors
	
	
	//Fields
	
	private var binding: BottomSheetBinding? = null
		get() = if (field?.root?.context == null) null else field
	
	override var onClosedCallback: (() -> Unit)? = null
	
	override var titleStringResource: Int? = null
	
	//.Fields
	
	
	//View configuring
	
	private fun initView() {
		val binding = BottomSheetBinding.inflate(
			LayoutInflater.from(context),
			null,
			false
		)
		this.binding = binding
		configureViews(binding)
		setOnClickListeners(binding)
		addView(binding.root)
	}
	
	private fun configureViews(binding: BottomSheetBinding) {
		val context = binding.root.context
		
		val title = binding.bottomSheetTitle
		title.text = titleStringResource?.let { context.resources.getString(it) }
		
		val rootLayout = binding.bottomSheetRoot
		rootLayout.background.alpha = 0
		
		val bottomSheetBehavior = getBottomSheetBehavior()
		bottomSheetBehavior?.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
			override fun onStateChanged(bottomSheet: View, newState: Int) {
				if (newState == BottomSheetBehavior.STATE_COLLAPSED) removeBottomSheetView()
			}
			override fun onSlide(bottomSheet: View, slideOffset: Float) {
				rootLayout.background.alpha = (255 * slideOffset).toInt()
			}
		})
		
		val bottomSheet = binding.bottomSheetBottomSheet
		val layoutTransition = bottomSheet.layoutTransition
		layoutTransition.setDuration(500)
		layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
	}
	
	private fun setOnClickListeners(binding: BottomSheetBinding) {
		binding.bottomSheetCloseButton.setOnClickListener { collapseBottomSheet() }
		binding.bottomSheetRoot.setOnClickListener { collapseBottomSheet() }
	}
	
	//.View configuring
	
	
	//Actions
	
	override fun inflateRootLayoutWith(view: View) {
		binding?.bottomSheetContentFrame?.removeAllViews()
		binding?.bottomSheetContentFrame?.addView(view)
	}
	
	override fun showBottomSheet() {
		Handler(Looper.getMainLooper()).postDelayed(
			{ getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_EXPANDED }, 0
		)
	}
	
	override fun collapseBottomSheet() {
		getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_COLLAPSED
	}
	
	//.Actions
	
	
	//Utils
	
	private fun getBottomSheetBehavior(): BottomSheetBehavior<ConstraintLayout>? {
		return binding?.let { binding ->
			BottomSheetBehavior.from(binding.bottomSheetBottomSheet)
		}
	}
	
	private fun removeBottomSheetView() {
		val parentView = binding?.root?.parent as? ViewGroup
		parentView?.removeView(this)
		binding = null
		onClosedCallback?.invoke()
	}
	
	//.Utils
}