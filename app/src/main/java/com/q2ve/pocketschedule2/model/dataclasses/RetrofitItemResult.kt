package com.q2ve.pocketschedule2.model.dataclasses

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

data class RetrofitItemResult <type>(
    val totalCount: Int?,
    val items: List<type>?
)