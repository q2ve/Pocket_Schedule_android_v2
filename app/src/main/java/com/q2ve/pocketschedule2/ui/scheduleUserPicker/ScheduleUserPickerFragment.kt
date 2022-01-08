package com.q2ve.pocketschedule2.ui.scheduleUserPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.ScheduleUserPickerBinding
import com.q2ve.pocketschedule2.helpers.allowBackButtonAction
import com.q2ve.pocketschedule2.ui.ButtonAnimator

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Background ellipse should have same position as in parent (if exist)")
class ScheduleUserPickerFragment: Fragment() {
	companion object {
		fun newInstance(allowReturn: Boolean): Fragment {
			val fragment = ScheduleUserPickerFragment()
			fragment.arguments = bundleOf(
				"allowReturn" to allowReturn
			)
			return fragment
		}
	}
	
	private lateinit var viewModel: ScheduleUserPickerViewModel
	
	private lateinit var enterButton: TextView
	private lateinit var backButton: ImageView
	private lateinit var universityField: TextView
	private lateinit var scheduleUserField: TextView
	private lateinit var spinner: ProgressBar
	private lateinit var errorField: TextView
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel = ViewModelProvider(this).get(ScheduleUserPickerViewModel::class.java)
		
		val binding = ScheduleUserPickerBinding.inflate(inflater, container, false)
		
		backButton = binding.scheduleUserPickerBackButton
		universityField = binding.scheduleUserPickerUniversityField
		scheduleUserField = binding.scheduleUserPickerScheduleUserField
		enterButton = binding.scheduleUserPickerEnterButton
		spinner = binding.scheduleUserPickerWaitingSpinner
		errorField = binding.scheduleUserPickerErrorTextview
		
		//Setting on click listeners and animations
		ButtonAnimator(enterButton).animateWeakPressing()
		ButtonAnimator(backButton).animateWeakPressing()
		
		val allowReturn = arguments?.getBoolean("allowReturn") ?: false
		if (allowReturn) backButton.visibility = View.VISIBLE
		allowBackButtonAction(allowReturn)
		
		removeSpinner()
		
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
		
		viewModel.scheduleUserName?.subscribe {
			bindNewScheduleUserName(it)
		}
		
		return binding.root
	}
	
	private fun bindNewUniversityName(name: String) { universityField.text = name }
	
	private fun bindNewScheduleUserName(name: String) { scheduleUserField.text = name }
	
	private fun makeErrorMessage(resource: Int) { errorField.text = resources.getString(resource) }
	
	private fun removeErrorMessage() { errorField.text = "" }
	
	private fun placeSpinner() {
		spinner.visibility = View.VISIBLE
		enterButton.text = ""
		enterButton.background?.setTint(
			ContextCompat.getColor(requireContext(), R.color.colorBlueLightBlocked)
		)
		enterButton.setOnClickListener {  }
		universityField.setOnClickListener {  }
		universityField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorLightGray1)
		)
		scheduleUserField.setOnClickListener {  }
		scheduleUserField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorLightGray1)
		)
		backButton.setOnClickListener {  }
	}
	
	private fun removeSpinner() {
		spinner.visibility = View.INVISIBLE
		enterButton.text = resources.getString(R.string.login_as_verb)
		enterButton.background?.setTint(
			ContextCompat.getColor(requireContext(), R.color.colorBlueLight)
		)
		enterButton.setOnClickListener { viewModel.enterButtonPressed() }
		universityField.setOnClickListener { viewModel.universityFieldPressed() }
		universityField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorBlack)
		)
		scheduleUserField.setOnClickListener { viewModel.scheduleUserFieldPressed() }
		scheduleUserField.setTextColor(
			ContextCompat.getColor(requireContext(), R.color.colorBlack)
		)
		backButton.setOnClickListener { viewModel.backButtonPressed() }
	}
	
	override fun onDestroy() {
		viewModel.onDestroyView()
		super.onDestroy()
	}
}