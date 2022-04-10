package com.q2ve.pocketschedule2.ui.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.q2ve.pocketschedule2.BuildConfig
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.SettingsBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator
import com.q2ve.pocketschedule2.helpers.hideKeyboard
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemUser
import com.q2ve.pocketschedule2.ui.core.CoreScreens

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class SettingsFragment: Fragment() {
	lateinit var binding: SettingsBinding
	lateinit var viewModel: SettingsViewModel
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = SettingsBinding.inflate(inflater, container, false)
		viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
		viewModel.onCreateView(::inflateViews)
		return binding.root
	}
	
	private fun inflateViews(user: RealmItemUser, startScreen: CoreScreens) {
		
		//userName
		val userNameTextView = binding.settingsCurrentUserName
		val name = user.firstName + " " + user.lastName
		userNameTextView.text = name
		
		//userGroup
		val userGroupTextView = binding.settingsCurrentUserGroup
		userGroupTextView.text = user.scheduleUser?.name
		
		//backButton
		val backButton = binding.settingsBackButton
		backButton.setOnClickListener { viewModel.onBackButtonPressed(requireActivity()) }
		ButtonAnimator(backButton).animateStrongPressing()
		
		//logoutButton
		val logoutButton = binding.settingsLogoutButton
		logoutButton.setOnClickListener { viewModel.onLogoutButtonPressed() }
		ButtonAnimator(logoutButton).animateStrongPressing()
		
		//serviceAuthModule
		val authServiceNameTextView = binding.settingsCurrentAuthService
		val serviceLoginField = binding.settingsAuthorizationLogin
		val servicePasswordField = binding.settingsAuthorizationPassword
		authServiceNameTextView.text = user.university?.serviceName ?: getString(R.string.authorize)
		val serviceLogin = user.serviceLogin
		serviceLogin?.let {
			serviceLoginField.hint = it
			servicePasswordField.hint = getString(R.string.password_dummy)
		}
		serviceLoginField.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) hideKeyboard()
		}
		servicePasswordField.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) hideKeyboard()
		}
		
		
		//serviceEnterButton
		val serviceEnterButton = binding.settingsAuthorizationSaveButton
		serviceEnterButton.setOnClickListener {
			viewModel.onEnterButtonPressed(
				serviceLoginField.text.toString(),
				servicePasswordField.text.toString()
			)
		}
		ButtonAnimator(serviceEnterButton)
		
		//scheduleUserSelection
		val currentScheduleUser = binding.settingsCurrentGroup
		currentScheduleUser.setOnClickListener {
			viewModel.onScheduleUserSelectorPressed()
		}
		val currentScheduleUniversity = binding.settingsCurrentUniversity
		if (user.vkId == null || user.vkId == 0) {
			binding.settingsCurrentUniversityModule.visibility = View.GONE
		} else {
			currentScheduleUniversity.setOnClickListener {
				viewModel.onUniversitySelectorPressed()
			}
		}
		
		//radioButtons
		val colors = ColorStateList(
			arrayOf(
				intArrayOf(-android.R.attr.state_checked), // unchecked
				intArrayOf(android.R.attr.state_checked) // checked
			), intArrayOf(
				resources.getColor(R.color.colorLightGray1, requireActivity().theme), // unchecked
				resources.getColor(R.color.colorBlue, requireActivity().theme) // checked
			)
		)
		val newsScreenRadio = binding.settingsStartScreenNews
		val scheduleScreenRadio = binding.settingsStartScreenSchedule
		val deadlinesScreenRadio = binding.settingsStartScreenDeadlines
		
		newsScreenRadio.buttonTintList = colors
		scheduleScreenRadio.buttonTintList = colors
		deadlinesScreenRadio.buttonTintList = colors
		
		selectStartScreen(startScreen)
		
		newsScreenRadio.setOnClickListener {
			viewModel.onStartScreenSelected(CoreScreens.News)
		}
		scheduleScreenRadio.setOnClickListener {
			viewModel.onStartScreenSelected(CoreScreens.Schedule)
		}
		deadlinesScreenRadio.setOnClickListener {
			viewModel.onStartScreenSelected(CoreScreens.Deadlines)
		}
		
		//aboutApp
		val appIcon = binding.settingsAppIcon
		ButtonAnimator(appIcon).animateStrongPressingWithFading()
		
		val versionTextView = binding.settingsAppVersion
		val version = versionTextView.text.toString() + BuildConfig.VERSION_NAME
		versionTextView.text = version
		
		val privacyPolicyLink = binding.settingsPrivacyPolicyLink
		privacyPolicyLink.movementMethod = LinkMovementMethod.getInstance()

		val feedbackLink = binding.settingsFeedbackLink
		feedbackLink.movementMethod = LinkMovementMethod.getInstance()
		
		//subscribing to viewModel's observables
		viewModel.selectedStartScreen?.subscribe { selectStartScreen(it) }
		viewModel.selectedServiceUser?.subscribe { setServiceUser(it) }
		viewModel.isSpinnerPlaced?.subscribe { if (it) placeSpinner() else removeSpinner() }
		viewModel.selectedUserName?.subscribe { userNameTextView.text = it }
		viewModel.selectedUserGroup?.subscribe { userGroupTextView.text = it }
		viewModel.authorizationErrorMessage?.subscribe {
			if (it == null) removeAuthorizationErrorMessage() else makeAuthorizationErrorMessage(it)
		}
		viewModel.selectionErrorMessage?.subscribe {
			if (it == null) removeSelectionErrorMessage() else makeSelectionErrorMessage(it)
		}
		viewModel.selectedScheduleUniversity?.subscribe {
			val theme = requireActivity().theme
			currentScheduleUniversity.setTextColor(resources.getColor(R.color.colorBlack, theme))
			currentScheduleUniversity.text = it
		}
		viewModel.selectedScheduleUser?.subscribe {
			val theme = requireActivity().theme
			if (it != null) {
				currentScheduleUser.setTextColor(resources.getColor(R.color.colorBlack, theme))
				currentScheduleUser.text = it
			} else {
				currentScheduleUser.setTextColor(resources.getColor(R.color.colorTextFieldText, theme))
				currentScheduleUser.text = getString(R.string.choose_your_group)
			}
		}
	}
	
	private fun selectStartScreen(screen: CoreScreens) {
		val newsScreenRadio = binding.settingsStartScreenNews
		val scheduleScreenRadio = binding.settingsStartScreenSchedule
		val deadlinesScreenRadio = binding.settingsStartScreenDeadlines
		
		newsScreenRadio.isChecked = false
		scheduleScreenRadio.isChecked = false
		deadlinesScreenRadio.isChecked = false
		
		when (screen) {
			CoreScreens.News -> newsScreenRadio.isChecked = true
			CoreScreens.Schedule -> scheduleScreenRadio.isChecked = true
			CoreScreens.Deadlines -> deadlinesScreenRadio.isChecked = true
			else -> scheduleScreenRadio.isChecked = true
		}
	}
	
	private fun setServiceUser(login: String) {
		val loginField = binding.settingsAuthorizationLogin
		val passwordField = binding.settingsAuthorizationPassword
		loginField.text = null
		passwordField.text = null
		loginField.hint = login
		passwordField.hint = getString(R.string.password_dummy)
	}
	
	private fun makeAuthorizationErrorMessage(errorMessage: Int) {
		val errorTextView = binding.settingsAuthorizationErrorTextview
		errorTextView.visibility = View.VISIBLE
		errorTextView.text = resources.getString(errorMessage)
	}
	
	private fun removeAuthorizationErrorMessage() {
		val errorTextView = binding.settingsAuthorizationErrorTextview
		errorTextView.visibility = View.GONE
		errorTextView.text = null
	}
	
	private fun makeSelectionErrorMessage(errorMessage: Int) {
		val errorTextView = binding.settingsSelectionErrorTextview
		errorTextView.visibility = View.VISIBLE
		errorTextView.text = resources.getString(errorMessage)
	}
	
	private fun removeSelectionErrorMessage() {
		val errorTextView = binding.settingsSelectionErrorTextview
		errorTextView.visibility = View.GONE
		errorTextView.text = null
	}
	
	private fun placeSpinner() {
		binding.settingsAuthorizationLogin.setTextColor(
			resources.getColor(R.color.colorTextFieldText, requireActivity().theme)
		)
		binding.settingsAuthorizationPassword.setTextColor(
			resources.getColor(R.color.colorTextFieldText, requireActivity().theme)
		)
		val serviceEnterButton = binding.settingsAuthorizationSaveButton
		serviceEnterButton.text = ""
		serviceEnterButton.background.setTint(
			resources.getColor(R.color.colorBlueLightBlocked, requireActivity().theme)
		)
		binding.settingsWaitingSpinner.visibility = View.VISIBLE
		binding.universityWaitingSpinner.visibility = View.VISIBLE
		binding.scheduleUserWaitingSpinner.visibility = View.VISIBLE
	}
	
	private fun removeSpinner() {
		binding.settingsAuthorizationLogin.setTextColor(
			resources.getColor(R.color.colorBlack, requireActivity().theme)
		)
		binding.settingsAuthorizationPassword.setTextColor(
			resources.getColor(R.color.colorBlack, requireActivity().theme)
		)
		val serviceEnterButton = binding.settingsAuthorizationSaveButton
		serviceEnterButton.text = resources.getString(R.string.save_data)
		serviceEnterButton.background.setTint(
			resources.getColor(R.color.colorBlueLight, requireActivity().theme)
		)
		binding.settingsWaitingSpinner.visibility = View.INVISIBLE
		binding.universityWaitingSpinner.visibility = View.INVISIBLE
		binding.scheduleUserWaitingSpinner.visibility = View.INVISIBLE
	}
	
	override fun onDestroyView() {
		viewModel.selectedStartScreen?.unsubscribeAll()
		viewModel.selectedServiceUser?.unsubscribeAll()
		viewModel.isSpinnerPlaced?.unsubscribeAll()
		viewModel.selectedUserName?.unsubscribeAll()
		viewModel.selectedUserGroup?.unsubscribeAll()
		viewModel.authorizationErrorMessage?.unsubscribeAll()
		viewModel.selectionErrorMessage?.unsubscribeAll()
		viewModel.selectedScheduleUniversity?.unsubscribeAll()
		viewModel.selectedScheduleUser?.unsubscribeAll()
		viewModel.onDestroyView()
		super.onDestroyView()
	}
}