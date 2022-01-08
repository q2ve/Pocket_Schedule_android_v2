package com.q2ve.pocketschedule2.ui.login.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.LoginViaUniversityBinding
import com.q2ve.pocketschedule2.helpers.hideKeyboard
import com.q2ve.pocketschedule2.ui.ButtonAnimator

class LoginViaUniversityFragment: Fragment() {
	companion object {
		fun newInstance(
			authorizationService: String,
			universityId: String
		): LoginViaUniversityFragment {
			val fragment = LoginViaUniversityFragment()
			fragment.arguments = bundleOf(
				"authorizationService" to authorizationService,
				"universityId" to universityId
			)
			return fragment
		}
	}
	
	private lateinit var viewModel: LoginViaUniversityViewModel
	
	private lateinit var loginField: EditText
	private lateinit var passwordField: EditText
	private lateinit var enterButton: TextView
	private lateinit var backButton: Button
	private lateinit var spinner: ProgressBar
	private lateinit var errorField: TextView
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = LoginViaUniversityBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(LoginViaUniversityViewModel::class.java)
		arguments?.getString("universityId")?.let { viewModel.universityId = it }
		
		//Setting title
		val titleField = binding.loginViaUniversityUniversityName
		titleField.text = arguments?.getString("authorizationService")
		
		//Setting on focus change listeners. They will hide keyboard when fields will loose focus.
		loginField = binding.loginViaUniversityLoginField
		loginField.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) hideKeyboard()
		}
		
		passwordField = binding.loginViaUniversityPasswordField
		passwordField.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) hideKeyboard()
		}
		
		//Setting on click listeners and animations
		enterButton = binding.loginViaUniversityEnterButton
		enterButton.setOnClickListener {
			enterButtonPressed(loginField.text.toString(), passwordField.text.toString())
		}
		ButtonAnimator(enterButton).animateWeakPressing()

		backButton = binding.loginViaUniversityBackButton
		backButton.setOnClickListener { backButtonPressed() }
		ButtonAnimator(backButton).animateWeakPressing()
		
		spinner = binding.loginViaUniversityWaitingSpinner
		
		errorField = binding.loginViaUniversityErrorTextview
		
		//Subscribing on viewModel fields
		viewModel.loadingSpinnerVisibility?.subscribe {
			if (it) placeSpinner() else removeSpinner()
		}
		
		viewModel.errorMessage?.subscribe {
			if (it == null) removeErrorMessage() else makeErrorMessage(it)
		}
		
		return binding.root
	}

	private fun enterButtonPressed (login: String, password: String) {
		viewModel.enterButtonPressed(login, password)
	}

	private fun backButtonPressed () {
		viewModel.backButtonPressed(requireActivity())
	}
	
	private fun makeErrorMessage(resource: Int) { errorField.text = getString(resource) }
	
	private fun removeErrorMessage() { errorField.text = "" }
	
	//TODO("Color changing animation. And better color for locked state")
	private fun placeSpinner() {
		spinner.visibility = View.VISIBLE
		enterButton.text = ""
		enterButton.background.setTint(
			ContextCompat.getColor(requireContext(), R.color.colorBlueLightBlocked)
		)
		enterButton.setOnClickListener {  }
		loginField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorLightGray1)
		)
		loginField.isEnabled = false
		passwordField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorLightGray1)
		)
		passwordField.isEnabled = false
		backButton.setOnClickListener {  }
	}
	
	private fun removeSpinner() {
		spinner.visibility = View.INVISIBLE
		enterButton.text = resources.getString(R.string.login_as_verb)
		enterButton.background.setTint(
			ContextCompat.getColor(requireContext(), R.color.colorBlueLight)
		)
		enterButton.setOnClickListener {
			enterButtonPressed(loginField.text.toString(), passwordField.text.toString())
		}
		loginField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorBlack)
		)
		loginField.isEnabled = true
		passwordField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorBlack)
		)
		passwordField.isEnabled = true
		backButton.setOnClickListener { backButtonPressed() }
	}
	
	override fun onDestroy() {
		super.onDestroy()
		viewModel.onDestroyView()
	}
}