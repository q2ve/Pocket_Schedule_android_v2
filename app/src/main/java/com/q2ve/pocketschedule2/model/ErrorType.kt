package com.q2ve.pocketschedule2.model

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

enum class ErrorType {
	NullObject,
	RealmError,
	UnknownExternalError,
	UnknownServerError,
	UnknownInternalError,
	NoInternetConnection,
	ValidationError,
	Throttling,
	UnknownHost,
	ConnectionTimeout,
	OutdatedSession,
	InvalidApiVersion,
	IncorrectObject
}