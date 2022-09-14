package com.q2ve.schedappv2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.helpers.Frames

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginNavigatorFragment: Fragment() {
	companion object {
		fun newInstance(startScreen: LoginScreens = LoginScreens.Default): Fragment {
			val fragment = LoginNavigatorFragment()
			fragment.arguments = bundleOf(
				"startScreen" to startScreen
			)
			return fragment
		}
	}
	
	private lateinit var viewModel: LoginNavigatorViewModel
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val startScreen = arguments?.get("startScreen") as LoginScreens
		viewModel = ViewModelProvider(this).get(LoginNavigatorViewModel::class.java)
		viewModel.startScreen = startScreen
		
		Frames.registerLoginFrame(R.id.login_navigation_frame)
		
		viewModel.onCreateView()
		
		return inflater.inflate(R.layout.login_navigator, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		viewModel.ellipseViewModel?.subscribe(::moveEllipse)
		
		viewModel.onViewCreated()
	}
	
	private fun getEllipse(): ImageView? {
		return view?.findViewById(R.id.schedule_user_picker_background_ellipse)
	}
	
	private fun moveEllipse(properties: BackgroundEllipseViewModel) {
		getEllipse()?.let { ellipse ->
			val multiplier = resources.displayMetrics.density
			ViewCompat.animate(ellipse)
				.rotation(properties.rotation)
				.translationX(properties.translationX * multiplier)
				.translationY(properties.translationY * multiplier)
				.setInterpolator(AccelerateDecelerateInterpolator())
				.setDuration(300)
				.start()
		}
	}
	
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		val backgroundEllipse = getEllipse()
		backgroundEllipse?.let {
			outState.putFloat("translationX", backgroundEllipse.translationX)
			outState.putFloat("translationY", backgroundEllipse.translationY)
			outState.putFloat("rotation", backgroundEllipse.rotation)
		}
	}
	
	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		//TODO("Not yet implemented.")
	}
	
	override fun onDestroy() {
		viewModel.ellipseViewModel?.unsubscribeAll()
		viewModel.onDestroyView()
		Frames.unregisterLoginFrame()
		super.onDestroy()
	}
}