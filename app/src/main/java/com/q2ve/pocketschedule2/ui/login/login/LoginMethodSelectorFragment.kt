package com.q2ve.pocketschedule2.ui.login.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.LoginMethodSelectorBinding
import com.q2ve.pocketschedule2.ui.ButtonAnimator

class LoginMethodSelectorFragment: Fragment() {
	private lateinit var binding: LoginMethodSelectorBinding
	private lateinit var viewModel: LoginMethodSelectorViewModel
	
	private lateinit var enterButton: ImageButton
	private lateinit var vkButton: TextView
	private lateinit var backButton: Button
	private lateinit var universitySelectorButton: ImageView
	private lateinit var universityTextView: TextView
	private lateinit var spinner: ProgressBar
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = LoginMethodSelectorBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(LoginMethodSelectorViewModel::class.java)
		
		//Setting on click listeners and animations
		enterButton = binding.loginMethodSelectorEnterButton
		enterButton.setOnClickListener { enterButtonPressed() }
		ButtonAnimator(enterButton)
		
		vkButton = binding.loginMethodSelectorVkButton
		vkButton.setOnClickListener { vkButtonPressed() }
		ButtonAnimator(vkButton)
		
		backButton = binding.loginMethodSelectorBackButton
		backButton.setOnClickListener { backButtonPressed() }
		ButtonAnimator(backButton)
		
		universitySelectorButton = binding.loginMethodSelectorUniversitySelectorButton
		universitySelectorButton.setOnClickListener { universitySelectorPressed() }
		ButtonAnimator(universitySelectorButton)
		
		universityTextView = binding.loginMethodSelectorLoginField
		universityTextView.setOnClickListener { universitySelectorPressed() }
		
		spinner = binding.loginMethodSelectorWaitingSpinner
		
		//Subscribing on viewModel fields
		viewModel.loadingSpinnerVisibility?.subscribe {
			if (it) placeSpinner() else removeSpinner()
		}
		
		viewModel.errorMessage?.subscribe {
			if (it == null) cleanErrorMessage() else makeErrorMessage(it)
		}
		
		viewModel.universityName?.subscribe {
			bindNewUniversityName(it)
		}
		
		return binding.root
	}
	
	private fun enterButtonPressed () {
		viewModel.enterButtonPressed()
	}
	
	private fun vkButtonPressed () {
		viewModel.vkButtonPressed(requireActivity())
	}
	
	private fun backButtonPressed () {
		viewModel.backButtonPressed()
	}
	
	private fun universitySelectorPressed() {
		viewModel.universitySelectorPressed()
	}
	
	private fun bindNewUniversityName(name: String) {
		universityTextView.text = name
	}
	
	private fun makeErrorMessage(stringResource: Int) {
		val errorTextView = binding.loginMethodSelectorErrorTextview
		errorTextView.text = getString(stringResource)
	}
	
	private fun cleanErrorMessage() {
		val errorTextView = binding.loginMethodSelectorErrorTextview
		errorTextView.text = ""
	}
	
	//TODO("Color changing animation. And better color for locked state")
	private fun placeSpinner() {
		spinner.visibility = View.VISIBLE
		enterButton.setOnClickListener(null)
		backButton.setOnClickListener(null)
		universitySelectorButton.setOnClickListener(null)
		universityTextView.setOnClickListener(null)
		vkButton.setOnClickListener(null)
		vkButton.text = ""
		vkButton.background.setTint(resources.getColor(R.color.colorBlueLocked))
	}
	
	private fun removeSpinner() {
		spinner.visibility = View.INVISIBLE
		enterButton.setOnClickListener { enterButtonPressed() }
		backButton.setOnClickListener { backButtonPressed() }
		universitySelectorButton.setOnClickListener { universitySelectorPressed() }
		universityTextView.setOnClickListener { universitySelectorPressed() }
		vkButton.setOnClickListener { vkButtonPressed() }
		vkButton.text = resources.getString(R.string.login_via_VK)
		vkButton.background.setTint(resources.getColor(R.color.colorBlue))
	}
	
	override fun onDestroy() {
		super.onDestroy()
		viewModel.onDestroyView()
	}
}