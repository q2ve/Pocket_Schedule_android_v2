package com.q2ve.schedappv2.model.dataclasses

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

data class RetrofitResponseDeadline (
	val result: RealmItemDeadline?,
	val error: RetrofitItemError?
)