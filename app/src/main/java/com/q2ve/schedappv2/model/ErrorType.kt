package com.q2ve.schedappv2.model

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