package com.q2ve.pocketschedule2.ui.recyclerSelector

import com.q2ve.pocketschedule2.helpers.Constants

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

/**
 * The controller will be unable to amend the recycler`s dataset if
 * the recyclerAdapter will not be provided in the constructor or later.
 */
abstract class RecyclerSelectorUploadingControllerBase(
	var recyclerAdapter: RecyclerSelectorAdapter?
) {
	protected var offset = 0
	protected val paginationStep = Constants.paginationStep
	protected var query = ""
	
	var onUploadMoreItemsCallback: (() -> Unit)? = null
	var onFillRecyclerDatasetCallback: (() -> Unit)? = null
	
	abstract fun uploadItems()
	
	fun searchItems(text: String) {
		query = text
		dropOffset()
		uploadItems()
	}
	
	open fun uploadMoreItems() {
		increaseOffset()
		onUploadMoreItemsCallback?.let { it() }
		uploadItems()
	}
	
	protected fun emptyRecyclerDataset() {
		recyclerAdapter?.stringsList = emptyList()
		recyclerAdapter?.notifyDataSetChanged()
	}
	
	protected fun fillRecyclerDataset(items: List<String>) {
		recyclerAdapter?. let { it.stringsList += items }
		recyclerAdapter?.notifyDataSetChanged()
		onFillRecyclerDatasetCallback?.let { it() }
	}
	
	private fun increaseOffset() {
		offset += paginationStep
	}
	
	private fun dropOffset() {
		offset = 0
	}
}