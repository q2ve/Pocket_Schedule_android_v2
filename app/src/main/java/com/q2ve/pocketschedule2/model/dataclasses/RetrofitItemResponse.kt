package com.q2ve.pocketschedule2.model.dataclasses

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RetrofitItemResponse <type> (
	val error: RetrofitItemError?,
	val result: RetrofitItemResult <type>?
)