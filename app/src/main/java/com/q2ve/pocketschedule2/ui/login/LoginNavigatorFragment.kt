package com.q2ve.pocketschedule2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.ui.Frames

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
		
		return inflater.inflate(R.layout.login_navigator, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		viewModel.ellipseTranslationX?.subscribe{
			getEllipse()?.translationX = (it * resources.displayMetrics.density)
		}
		viewModel.ellipseTranslationY?.subscribe{
			getEllipse()?.translationY = (it * resources.displayMetrics.density)
		}
		viewModel.ellipseRotation?.subscribe{
			getEllipse()?.rotation = (it * resources.displayMetrics.density)
		}
		
		viewModel.viewCreated()
	}
	
	private fun getEllipse(): ImageView? {
		return view?.findViewById(R.id.schedule_user_picker_background_ellipse)
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
		super.onDestroy()
		Frames.unregisterLoginFrame()
		viewModel.onDestroyView()
	}
}