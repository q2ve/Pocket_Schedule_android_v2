package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.q2ve.pocketschedule2.databinding.DeadlinesItemBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//TODO("Linked tag should be replaced with urgency tag with same icon.
//      It will have three-colours gradation.
//      Green - all ok, Yellow - less than 3 days before deadline, Red - for today`s deadlines.
//      Outdated deadlines will have red colour too. Closed deadlines will free from this tag.")

class DeadlinesPageModuleAdapter(
	private val onDeadlineClicked: (RealmItemDeadline) -> Unit,
	private val onDeadlineCheckboxClicked: (RealmItemDeadline) -> Unit,
	realmResults: RealmResults<RealmItemDeadline>
): RealmRecyclerViewAdapter<RealmItemDeadline,DeadlinesPageModuleAdapter.RecyclerItemHolder>(
	realmResults,
	true,
	true
) {
	class RecyclerItemHolder(
		viewBinding: DeadlinesItemBinding
	): RecyclerView.ViewHolder(viewBinding.root) {
		var statusNormal: ImageView = viewBinding.coreDeadlinesItemStatusNormal
		var statusClosed: ImageView = viewBinding.coreDeadlinesItemStatusClosed
		var statusExpired: ImageView = viewBinding.coreDeadlinesItemStatusExpired
		var title: TextView = viewBinding.coreDeadlinesItemTitle
		var subject: TextView = viewBinding.coreDeadlinesItemSubject
		var tagFile: ImageView = viewBinding.coreDeadlinesItemTagFile
		var tagDescription: ImageView = viewBinding.coreDeadlinesItemTagDescription
		var tagLinked: ImageView = viewBinding.coreDeadlinesItemTagLinked
		var checkbox: ImageView = viewBinding.coreDeadlinesItemCheckbox
		var checkboxChecked: ImageView = viewBinding.coreDeadlinesItemCheckboxChecked
	}
	
	override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerItemHolder {
		val binding = DeadlinesItemBinding.inflate(
			LayoutInflater.from(container.context),
			container,
			false
		)
		return RecyclerItemHolder(binding)
	}
	
	override fun onBindViewHolder(holder: RecyclerItemHolder, position: Int) {
		getItem(position)?.let { deadline ->
			val currentTime = (System.currentTimeMillis()/1000).toInt()
			
			//statusImage
			if (deadline.isClosed == true) {
				holder.statusNormal.visibility = View.INVISIBLE
				holder.statusClosed.visibility = View.VISIBLE
			} else if (deadline.endDate?: 0 < currentTime) {
				holder.statusNormal.visibility = View.INVISIBLE
				holder.statusExpired.visibility = View.VISIBLE
			}
			
			//title
			holder.title.text = deadline.title
			
			//subject
			holder.subject.text = deadline.subject?.name
			
			//tagFile TODO("after functional of files will be added")
			holder.tagFile.visibility = View.GONE
			
			//tagDescription
			if (deadline.description == null || deadline.description == "") {
				holder.tagDescription.visibility = View.GONE
			}
			
			//tagLinked TODO("It should be displayed when subject exist. But why? We already have subject as string")
			holder.tagLinked.visibility = View.GONE
			
			//checkbox
			if (deadline.isExternal == true || deadline.isExternal == null) {
				holder.checkbox.visibility = View.INVISIBLE
				holder.checkbox.isClickable = false
				holder.checkboxChecked.isClickable = false
			}
			if (deadline.isClosed == true) {
				holder.checkbox.visibility = View.INVISIBLE
				holder.checkboxChecked.visibility = View.VISIBLE
			}
			
			//deadline on click listener
			ButtonAnimator(holder.itemView).animateWeakPressing()
			holder.itemView.setOnClickListener {
				onDeadlineClicked(deadline)
			}
			
			//checkbox on click listener
			holder.checkbox.setOnClickListener {
				holder.checkbox.visibility = View.INVISIBLE
				holder.checkboxChecked.visibility = View.VISIBLE
				holder.statusNormal.visibility = View.INVISIBLE
				holder.statusClosed.visibility = View.VISIBLE
				holder.statusExpired.visibility = View.INVISIBLE
				Handler().postDelayed({ onDeadlineCheckboxClicked(deadline) }, 250)
			}
			
			//checked checkbox on click listener
			holder.checkboxChecked.setOnClickListener {
				holder.checkbox.visibility = View.VISIBLE
				holder.checkboxChecked.visibility = View.INVISIBLE
				holder.statusClosed.visibility = View.INVISIBLE
				if (deadline.endDate?: 0 < currentTime) {
					holder.statusExpired.visibility = View.VISIBLE
					holder.statusNormal.visibility = View.INVISIBLE
				} else {
					holder.statusExpired.visibility = View.INVISIBLE
					holder.statusNormal.visibility = View.VISIBLE
				}
				Handler().postDelayed({ onDeadlineCheckboxClicked(deadline) }, 250)
			}
		}
		
	}
}

class DEPRECATEDDeadlinesPageModuleAdapter(
	private val onDeadlineClicked: (RealmItemDeadline) -> Unit,
	private val onDeadlineCheckboxClicked: (RealmItemDeadline) -> Unit,
	private val deadlines: MutableList<RealmItemDeadline>,
): RecyclerView.Adapter<DEPRECATEDDeadlinesPageModuleAdapter.RecyclerItemHolder>() {
	
	class RecyclerItemHolder(
		viewBinding: DeadlinesItemBinding
	): RecyclerView.ViewHolder(viewBinding.root) {
		var statusNormal: ImageView = viewBinding.coreDeadlinesItemStatusNormal
		var statusClosed: ImageView = viewBinding.coreDeadlinesItemStatusClosed
		var statusExpired: ImageView = viewBinding.coreDeadlinesItemStatusExpired
		var title: TextView = viewBinding.coreDeadlinesItemTitle
		var subject: TextView = viewBinding.coreDeadlinesItemSubject
		var tagFile: ImageView = viewBinding.coreDeadlinesItemTagFile
		var tagDescription: ImageView = viewBinding.coreDeadlinesItemTagDescription
		var tagLinked: ImageView = viewBinding.coreDeadlinesItemTagLinked
		var checkbox: ImageView = viewBinding.coreDeadlinesItemCheckbox
		var checkboxChecked: ImageView = viewBinding.coreDeadlinesItemCheckboxChecked
	}

	override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerItemHolder {
		val binding = DeadlinesItemBinding.inflate(
			LayoutInflater.from(container.context),
			container,
			false
		)
		return RecyclerItemHolder(binding)
	}

	override fun onBindViewHolder(holder: RecyclerItemHolder, position: Int) {
		val deadline = deadlines[position]
		val currentTime = (System.currentTimeMillis()/1000).toInt()

		//statusImage
		if (deadline.isClosed == true) {
			holder.statusNormal.visibility = View.INVISIBLE
			holder.statusClosed.visibility = View.VISIBLE
		} else if (deadline.endDate?: 0 < currentTime) {
			holder.statusNormal.visibility = View.INVISIBLE
			holder.statusExpired.visibility = View.VISIBLE
		}

		//title
		holder.title.text = deadline.title

		//subject
		holder.subject.text = deadline.subject?.name

		//tagFile TODO("after functional of files will be added")
		holder.tagFile.visibility = View.GONE

		//tagDescription
		if (deadline.description == null || deadline.description == "") {
			holder.tagDescription.visibility = View.GONE
		}

		//tagLinked TODO("It should be displayed when subject exist. But why? We already have subject as string")
		holder.tagLinked.visibility = View.GONE

		//checkbox
		if (deadline.isExternal == true || deadline.isExternal == null) {
			holder.checkbox.visibility = View.INVISIBLE
			holder.checkbox.isClickable = false
			holder.checkboxChecked.isClickable = false
		}
		if (deadline.isClosed == true) {
			holder.checkbox.visibility = View.INVISIBLE
			holder.checkboxChecked.visibility = View.VISIBLE
		}

		//deadline on touch listener
		val defaultScaleX = holder.itemView.scaleX
		val defaultScaleY = holder.itemView.scaleY

		holder.itemView.setOnTouchListener { view, event ->
			fun setDefaultProperties(view: View) {
				view.scaleX = defaultScaleX
				view.scaleY = defaultScaleY
			}
			when (event.action) {
				MotionEvent.ACTION_DOWN -> {
					view.scaleX *= 0.97f
					view.scaleY *= 0.97f
				}
				MotionEvent.ACTION_UP -> {
					setDefaultProperties(view)
					onDeadlineClicked(deadlines[position])
					view.performClick()
				}
				MotionEvent.ACTION_CANCEL -> {
					setDefaultProperties(view)
				}
			}
			true
		}

		//checkbox on click listener
		holder.checkbox.setOnClickListener {
			holder.checkbox.visibility = View.INVISIBLE
			holder.checkboxChecked.visibility = View.VISIBLE
			holder.statusNormal.visibility = View.INVISIBLE
			holder.statusClosed.visibility = View.VISIBLE
			holder.statusExpired.visibility = View.INVISIBLE
			Handler().postDelayed({ onDeadlineCheckboxClicked(deadline) }, 250)
		}

		//checked checkbox on click listener
		holder.checkboxChecked.setOnClickListener {
			holder.checkbox.visibility = View.VISIBLE
			holder.checkboxChecked.visibility = View.INVISIBLE
			holder.statusClosed.visibility = View.INVISIBLE
			if (deadline.endDate?: 0 < currentTime) {
				holder.statusExpired.visibility = View.VISIBLE
				holder.statusNormal.visibility = View.INVISIBLE
			} else {
				holder.statusExpired.visibility = View.INVISIBLE
				holder.statusNormal.visibility = View.VISIBLE
			}
			Handler().postDelayed({ onDeadlineCheckboxClicked(deadline) }, 250)
		}
	}

	override fun getItemCount() = deadlines.size
	
	fun replaceItem(deadline: RealmItemDeadline?, index: Int) {
		(this.itemCount > index).let {
			if (deadline == null) {
				deadlines.removeAt(index)
				notifyItemRemoved(index)
			} else {
				deadlines[index] = deadline
				notifyItemChanged(index)
			}
		}
	}
	
	fun addItem(deadline: RealmItemDeadline) {
		Log.e("addItem", deadline.toString())
		deadlines.add(deadline)
		notifyItemInserted(deadlines.size - 1)
	}
}