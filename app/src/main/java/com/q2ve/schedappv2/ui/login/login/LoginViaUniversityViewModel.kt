package com.q2ve.schedappv2.ui.login.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.helpers.Observable
import com.q2ve.schedappv2.model.ErrorType
import com.q2ve.schedappv2.model.Model
import com.q2ve.schedappv2.model.dataclasses.RealmItemMain

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class LoginViaUniversityViewModel: ViewModel() {
	lateinit var universityId: String
	private val router = LoginViaUniversityRouter()
	
	var errorMessage: Observable<Int?>? = Observable(null)
	var loadingSpinnerVisibility: Observable<Boolean>? = Observable(false)
	
	private fun showSpinner() { loadingSpinnerVisibility?.value = true }
	
	private fun hideSpinner() { loadingSpinnerVisibility?.value = false }
	
	private fun makeErrorMessage(stringId: Int) { errorMessage?.value = stringId }
	
	private fun removeErrorMessage() { errorMessage?.value = null }
	
	fun enterButtonPressed(login: String, password: String) {
		when {
			(login.isEmpty() && password.isEmpty()) -> makeErrorMessage(R.string.how_to_login_without)
			(login.isEmpty()) -> makeErrorMessage(R.string.login_required)
			(password.isEmpty()) -> makeErrorMessage(R.string.password_required)
			else -> postUser(login, password)
		}
	}
	
	fun backButtonPressed(activity: Activity) { router.goBackToLoginMethodSelector(activity) }
	
	private fun postUser(login: String, password: String) {
		removeErrorMessage()
		showSpinner()
		
		fun onError(errorType: ErrorType) {
			hideSpinner()
			when (errorType) {
				ErrorType.RealmError -> makeErrorMessage(R.string.an_error_has_occurred_try_again)
				ErrorType.ValidationError -> makeErrorMessage(R.string.validation_error)
				ErrorType.NoInternetConnection -> Unit //This error will be displayed as toast
				else -> makeErrorMessage(R.string.server_error)
			}
		}
		fun onSuccess(mainObject: RealmItemMain) {
			val user = mainObject.currentUser
			if (user?.scheduleUser == null || user.university == null) {
				router.openScheduleUserPicker()
			}
			else router.goToCoreFragments()
		}
		
		if (::universityId.isInitialized) {
			Model(::onError).postUser(login, password, universityId, ::onSuccess)
		} else makeErrorMessage(R.string.an_error_has_occurred_try_again)
	}

	fun onDestroyView() {
		//TODO("Проверить, нужно ли это")
		errorMessage = null
		loadingSpinnerVisibility = null
	}
}