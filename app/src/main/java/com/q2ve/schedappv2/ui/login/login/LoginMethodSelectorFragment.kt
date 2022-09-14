package com.q2ve.schedappv2.ui.login.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.databinding.LoginMethodSelectorBinding
import com.q2ve.schedappv2.helpers.ButtonAnimator
import com.q2ve.schedappv2.ui.login.BackgroundEllipseObservable
import com.q2ve.schedappv2.ui.login.BackgroundEllipseProperties

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginMethodSelectorFragment: Fragment() {
	private lateinit var viewModel: LoginMethodSelectorViewModel
	
	private lateinit var enterButton: ImageButton
	private lateinit var vkButton: TextView
	private lateinit var backButton: Button
	private lateinit var universitySelectorButton: ImageView
	private lateinit var universityTextView: TextView
	private lateinit var spinner: ProgressBar
	private lateinit var errorField: TextView
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = LoginMethodSelectorBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(LoginMethodSelectorViewModel::class.java)
		
		//Setting on click listeners and animations
		enterButton = binding.loginMethodSelectorEnterButton
		enterButton.setOnClickListener { enterButtonPressed() }
		ButtonAnimator(enterButton).animateWeakPressing()
		
		vkButton = binding.loginMethodSelectorVkButton
		vkButton.setOnClickListener { vkButtonPressed() }
		ButtonAnimator(vkButton).animateWeakPressing()
		
		backButton = binding.loginMethodSelectorBackButton
		backButton.setOnClickListener { backButtonPressed() }
		ButtonAnimator(backButton).animateWeakPressing()
		
		universitySelectorButton = binding.loginMethodSelectorUniversitySelectorButton
		universitySelectorButton.setOnClickListener { universitySelectorPressed() }
		ButtonAnimator(universitySelectorButton).animateWeakPressing()
		
		universityTextView = binding.loginMethodSelectorLoginField
		universityTextView.setOnClickListener { universitySelectorPressed() }
		
		spinner = binding.loginMethodSelectorWaitingSpinner
		
		errorField = binding.loginMethodSelectorErrorTextview
		
		//Subscribing on viewModel fields
		viewModel.loadingSpinnerVisibility?.subscribe {
			if (it) placeSpinner() else removeSpinner()
		}
		
		viewModel.errorMessage?.subscribe {
			if (it == null) removeErrorMessage() else makeErrorMessage(it)
		}
		
		viewModel.universityName?.subscribe {
			bindNewUniversityName(it)
		}
		
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		BackgroundEllipseObservable.changeProperties(BackgroundEllipseProperties.loginMethodSelector)
	}
	
	private fun enterButtonPressed () {
		viewModel.enterButtonPressed()
	}
	
	private fun vkButtonPressed () {
		viewModel.vkButtonPressed(requireActivity())
	}
	
	private fun backButtonPressed () {
		viewModel.backButtonPressed(requireActivity())
	}
	
	private fun universitySelectorPressed() {
		viewModel.universitySelectorPressed()
	}
	
	private fun bindNewUniversityName(name: String) {
		universityTextView.text = name
	}
	
	private fun makeErrorMessage(resource: Int) { errorField.text = getString(resource) }
	
	private fun removeErrorMessage() { errorField.text = "" }
	
	//TODO("Color changing animation. And better color for locked state")
	private fun placeSpinner() {
		spinner.visibility = View.VISIBLE
		enterButton.setOnClickListener(null)
		backButton.setOnClickListener(null)
		universitySelectorButton.setOnClickListener(null)
		universityTextView.setOnClickListener(null)
		vkButton.setOnClickListener(null)
		vkButton.text = ""
		vkButton.background.setTint(
			ContextCompat.getColor(requireContext(), R.color.colorBlueLocked)
		)
	}
	
	private fun removeSpinner() {
		spinner.visibility = View.INVISIBLE
		enterButton.setOnClickListener { enterButtonPressed() }
		backButton.setOnClickListener { backButtonPressed() }
		universitySelectorButton.setOnClickListener { universitySelectorPressed() }
		universityTextView.setOnClickListener { universitySelectorPressed() }
		vkButton.setOnClickListener { vkButtonPressed() }
		vkButton.text = resources.getString(R.string.login_via_VK)
		vkButton.background.setTint(
			ContextCompat.getColor(requireContext(), R.color.colorBlue)
		)
	}
	
	override fun onDestroy() {
		viewModel.loadingSpinnerVisibility?.unsubscribeAll()
		viewModel.errorMessage?.unsubscribeAll()
		viewModel.universityName?.unsubscribeAll()
		viewModel.onDestroyView()
		super.onDestroy()
	}
}