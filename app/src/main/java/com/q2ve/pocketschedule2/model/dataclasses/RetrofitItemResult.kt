package com.q2ve.pocketschedule2.model.dataclasses

data class RetrofitItemResult <type>(
    val totalCount: Int?,
    val items: List<type>?
)