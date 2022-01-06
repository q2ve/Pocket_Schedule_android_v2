package com.q2ve.pocketschedule2.model

import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.ui.OutdatedAppNotification

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class ErrorResolver {
	fun resolveRetrofitError(errorType: ErrorType, onError: (ErrorType) -> Unit) {
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
}