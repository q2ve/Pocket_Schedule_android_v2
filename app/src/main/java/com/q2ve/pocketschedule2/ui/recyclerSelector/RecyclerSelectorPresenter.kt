package com.q2ve.pocketschedule2.ui.recyclerSelector

import android.animation.LayoutTransition
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.q2ve.pocketschedule2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RecyclerSelectorPresenter(
	private val container: ViewGroup,
	private val fragment: Fragment,
	private val uploadingController: RecyclerSelectorUploadingControllerBase,
	private val onClickCallback: (Int) -> Unit,
) {
	private lateinit var recyclerLayout: View
	private lateinit var adapter: RecyclerSelectorAdapter
	
	fun placeRecycler() {
		val layout = R.layout.recycler_selector
		recyclerLayout = fragment.layoutInflater.inflate(layout, container, false)
		
		//EnableTransitionType is necessary to xml "animateLayoutChanges" works with height changing
		val frame = recyclerLayout.findViewById<FrameLayout>(R.id.recycler_frame)
		frame.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
		frame.layoutTransition.setDuration(600)
		
		val recycler = recyclerLayout.findViewById<RecyclerView>(R.id.recycler_recyclerview)
		val spinner = recyclerLayout.findViewById<ProgressBar>(R.id.recycler_uploading_progressbar)
		val layoutManager = LinearLayoutManager(fragment.context)
		adapter = RecyclerSelectorAdapter(emptyList(), onClickCallback)
		uploadingController.recyclerAdapter = adapter
		val onScrollListener = RecyclerSelectorOnScrollListener(
			layoutManager,
			uploadingController::uploadMoreItems
		)
		uploadingController.onUploadMoreItemsCallback = {
			spinner.visibility = View.VISIBLE
			val temp = frame.layoutParams
			temp.height = frame.height
			frame.layoutParams = temp
		}
		uploadingController.onFillRecyclerDatasetCallback = {
			spinner.visibility = View.GONE
			val temp = frame.layoutParams
			temp.height = FrameLayout.LayoutParams.WRAP_CONTENT
			frame.layoutParams = temp
		}
		recycler.layoutManager = layoutManager
		recycler.adapter = adapter
		recycler.addOnScrollListener(onScrollListener)
		container.addView(recyclerLayout)
		uploadingController.uploadItems()
	}
	
	fun removeRecycler() {
		if (this::recyclerLayout.isInitialized) container.removeView(recyclerLayout)
	}
	
	fun stopUploading() {
		uploadingController.recyclerAdapter = null
	}
	
	fun continueUploading() {
		if (this::adapter.isInitialized) uploadingController.recyclerAdapter = adapter
	}
}