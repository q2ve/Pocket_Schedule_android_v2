package com.q2ve.pocketschedule2.ui.recyclerSelector

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerSelectorOnScrollListener(
	private val layoutManager: LinearLayoutManager,
	private val uploadMoreItems: () -> Unit
): RecyclerView.OnScrollListener() {
	var allowAddingItems = true
	var previousItemCount = 0
	
	override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
		super.onScrolled(recyclerView, dx, dy)
		val itemCount = recyclerView.adapter!!.itemCount
		if (previousItemCount == 0 && itemCount > 0) {
			previousItemCount = itemCount
		}
		if (layoutManager.findLastVisibleItemPosition() >= itemCount - 5) {
			if (allowAddingItems) {
				uploadMoreItems()
				allowAddingItems = false
			}
			if (itemCount != previousItemCount)	{
				allowAddingItems = true
			}
			previousItemCount = itemCount
		}
	}
}