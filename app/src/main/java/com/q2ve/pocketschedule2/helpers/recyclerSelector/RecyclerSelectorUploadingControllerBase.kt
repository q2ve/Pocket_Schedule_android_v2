package com.q2ve.pocketschedule2.helpers.recyclerSelector

import com.q2ve.pocketschedule2.helpers.Constants

abstract class RecyclerSelectorUploadingControllerBase(
	private val recyclerAdapter: RecyclerSelectorAdapter
) {
	protected var offset = 0
	protected val paginationStep = Constants.paginationStep
	protected var query = ""
	
	abstract fun uploadItems()
	
	fun searchItems(text: String) {
		query = text
		dropOffset()
		uploadItems()
	}
	
	open fun uploadMoreItems() {
		increaseOffset()
		uploadItems()
	}
	
	protected fun emptyRecyclerDataset() {
		recyclerAdapter.stringsList = emptyList()
		recyclerAdapter.notifyDataSetChanged()
	}
	
	protected fun fillRecyclerDataset(items: List<String>) {
		recyclerAdapter.stringsList += items
		recyclerAdapter.notifyDataSetChanged()
	}
	
	private fun increaseOffset() {
		offset += paginationStep
	}
	
	private fun dropOffset() {
		offset = 0
	}
}