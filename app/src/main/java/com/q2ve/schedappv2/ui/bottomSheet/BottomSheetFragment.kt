package com.q2ve.schedappv2.ui.bottomSheet

import android.animation.LayoutTransition
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.q2ve.schedappv2.databinding.BottomSheetBinding

class BottomSheetFragment: Fragment(), BottomSheetContract.View {
	
	companion object {
		const val ARGUMENT_TITLE_STRING_RESOURCE = "titleStringResource"
		
		fun newInstance(titleStringResource: Int?): BottomSheetFragment {
			val args = bundleOf(
				ARGUMENT_TITLE_STRING_RESOURCE to titleStringResource
			)
			val fragment = BottomSheetFragment()
			fragment.arguments = args
			return fragment
		}
	}
	
	
	//Fields
	
	private var binding: BottomSheetBinding? = null
		get() = if (field?.root?.context == null) null else field
	
	override var onClosedCallback: (() -> Unit)? = null
	
	private var titleStringIdObservable = MutableLiveData<Int?>()
	
	//.Fields
	
	
	//Lifecycle callbacks
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = BottomSheetBinding.inflate(
			LayoutInflater.from(context),
			null,
			false
		)
		this.binding = binding
		configureViews(binding)
		setOnClickListeners(binding)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		showBottomSheet()
	}
	
	override fun onDestroyView() {
		binding = null
		super.onDestroyView()
	}
	
	//.Lifecycle callbacks
	
	
	//View configuring
	
	private fun configureViews(binding: BottomSheetBinding) {
		val context = binding.root.context
		
		val titleTextView = binding.bottomSheetTitle
		titleStringIdObservable.value = arguments?.getInt(ARGUMENT_TITLE_STRING_RESOURCE)
		titleStringIdObservable.observe(viewLifecycleOwner) { titleStringResource ->
			titleTextView.text = titleStringResource?.let { context.resources.getString(it) }
		}
		
		val rootLayout = binding.bottomSheetRoot
		rootLayout.background.alpha = 0
		
		val bottomSheetBehavior = getBottomSheetBehavior()
		bottomSheetBehavior?.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
			override fun onStateChanged(bottomSheet: View, newState: Int) {
				if (newState == BottomSheetBehavior.STATE_COLLAPSED) removeBottomSheet()
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
	
	override fun inflate(view: View) {
		var observer: LifecycleEventObserver? = null
		observer = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
			if (event == Lifecycle.Event.ON_START) {
				binding?.bottomSheetContentFrame?.removeAllViews()
				binding?.bottomSheetContentFrame?.addView(view)
				observer?.let { this.lifecycle.removeObserver(it) }
				observer = null
			}
		}
		observer?.let { this.lifecycle.addObserver(it) }
	}
	
	override fun setTitle(titleStringId: Int?) {
		this.titleStringIdObservable.value = titleStringId
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
	
	private fun showBottomSheet() {
		Handler(Looper.getMainLooper()).postDelayed(
			{ getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_EXPANDED }, 0
		)
	}
	
	private fun removeBottomSheet() {
		parentFragmentManager.beginTransaction().remove(this).commit()
		onClosedCallback?.invoke()
	}
	
	//.Utils
}