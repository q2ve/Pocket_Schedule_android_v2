package com.q2ve.schedappv2.model

import com.q2ve.schedappv2.R
import com.q2ve.schedappv2.helpers.UserObserver
import com.q2ve.schedappv2.ui.OutdatedAppNotification
import com.q2ve.schedappv2.ui.popup.PopupMessagePresenter

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ErrorDeclarer {
	fun declareRetrofitError(errorType: ErrorType, onError: (ErrorType) -> Unit) {
		var outputErrorType = ErrorType.UnknownServerError
		//TODO("Как добавится проверка интернета - выставить актуально типы ошибок. Разделить ошибку интернета и сервера.")
		when (errorType) {
			ErrorType.Throttling -> outputErrorType = ErrorType.UnknownServerError //TODO("Повтор запроса через Handler().postDelayed({  }, 30) ")
			ErrorType.OutdatedSession -> UserObserver.logoutUser()
			ErrorType.UnknownExternalError -> outputErrorType = ErrorType.NoInternetConnection
			ErrorType.ConnectionTimeout -> outputErrorType = ErrorType.NoInternetConnection
			ErrorType.UnknownHost -> outputErrorType = ErrorType.NoInternetConnection
			ErrorType.InvalidApiVersion -> OutdatedAppNotification().show()
			else -> outputErrorType = errorType
		}
		onError(outputErrorType)
	}
	
	fun declareIncorrectObjectError() {
		val errorMessageId = R.string.some_items_not_loaded
		PopupMessagePresenter().createMessageSmall(true, errorMessageId, 5000)
	}
}