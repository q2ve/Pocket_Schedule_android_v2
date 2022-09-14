package com.q2ve.schedappv2.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.databinding.CoreNavigatorBinding
import com.q2ve.schedappv2.helpers.Frames

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class CoreNavigatorFragment: Fragment() {
	companion object {
		fun newInstance(startScreen: CoreScreens = CoreScreens.Default): Fragment {
			val fragment = CoreNavigatorFragment()
			fragment.arguments = bundleOf(
				"startScreen" to startScreen
			)
			return fragment
		}
	}
	
	private lateinit var viewModel: CoreNavigatorViewModel
	private lateinit var testBinding: CoreNavigatorBinding
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val startScreen = arguments?.get("startScreen") as CoreScreens
		viewModel = ViewModelProvider(this).get(CoreNavigatorViewModel::class.java)
		viewModel.startScreen = startScreen
		
		Frames.registerCoreFrame(R.id.core_navigation_frame)
		
		val binding = CoreNavigatorBinding.inflate(inflater, container, false)
		testBinding = binding
		viewModel.onCreateView(inflater, binding.coreNavbarFrame, resources, requireActivity().theme)
		
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated()
	}
	
	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		//TODO("Not yet implemented.")
	}
	
	override fun onViewStateRestored(savedInstanceState: Bundle?) {
		super.onViewStateRestored(savedInstanceState)
		//TODO("Not yet implemented.")
	}
	
	override fun onDestroy() {
		Frames.unregisterCoreFrame()
		super.onDestroy()
	}
}