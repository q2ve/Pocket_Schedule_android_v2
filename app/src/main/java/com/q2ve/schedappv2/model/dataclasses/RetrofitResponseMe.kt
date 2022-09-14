package com.q2ve.schedappv2.model.dataclasses

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

data class RetrofitResponseMe (
	val error: RetrofitItemError?,
	val result: RealmItemUser?
)