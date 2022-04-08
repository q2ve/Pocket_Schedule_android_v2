package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.q2ve.pocketschedule2.databinding.DeadlinesItemBinding
import com.q2ve.pocketschedule2.helpers.ButtonAnimator
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import io.realm.OrderedCollectionChangeSet
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
	private val realmResults: RealmResults<RealmItemDeadline>,
	private val onEmpty: (() -> Unit)? = null,
	private val onEmptinessFilled: (() -> Unit)? = null
): RealmRecyclerViewAdapter<RealmItemDeadline,DeadlinesPageModuleAdapter.RecyclerItemHolder>(
	realmResults,
	true,
	true
) {
	//BEWARE! GOVNOKOD!
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
	
	private var isOnEmptyCalled: Boolean = false
	
	init {
		realmResults.addChangeListener { data, changeSet ->
			if (changeSet.state == OrderedCollectionChangeSet.State.UPDATE) {
				if (data.isEmpty()) {
					if (!isOnEmptyCalled) {
						isOnEmptyCalled = true
						onEmpty?.invoke()
					} else {
						isOnEmptyCalled = false
						onEmptinessFilled?.invoke()
					}
				} else if (isOnEmptyCalled) {
					isOnEmptyCalled = false
					onEmptinessFilled?.invoke()
				}
			}
		}
	}
	
	override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
		super.onAttachedToRecyclerView(recyclerView)
		Handler().postDelayed(
			{
				if (data?.isValid == true) {
					if (data == null || data?.isEmpty() == true) {
						onEmpty?.invoke()
						isOnEmptyCalled = true
					}
				}
			},
			10
		)
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
			when {
				deadline.isClosed == true -> {
					holder.statusNormal.visibility = View.INVISIBLE
					holder.statusClosed.visibility = View.VISIBLE
					holder.statusExpired.visibility = View.INVISIBLE
				}
				deadline.endDate?: 0 < currentTime -> {
					holder.statusNormal.visibility = View.INVISIBLE
					holder.statusClosed.visibility = View.INVISIBLE
					holder.statusExpired.visibility = View.VISIBLE
				}
				else -> {
					holder.statusNormal.visibility = View.VISIBLE
					holder.statusClosed.visibility = View.INVISIBLE
					holder.statusExpired.visibility = View.INVISIBLE
				}
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
			} else holder.tagDescription.visibility = View.VISIBLE
			
			//tagLinked TODO("It should be displayed when subject exist. But why? We already have subject as string")
			holder.tagLinked.visibility = View.GONE
			
			//checkbox
			if (deadline.isExternal == true || deadline.isExternal == null) {
				holder.checkbox.visibility = View.INVISIBLE
				holder.checkbox.isClickable = false
				holder.checkboxChecked.isClickable = false
			} else {
				holder.checkbox.isClickable = true
				holder.checkboxChecked.isClickable = true
			}
			if (deadline.isClosed == true) {
				holder.checkbox.visibility = View.INVISIBLE
				holder.checkboxChecked.visibility = View.VISIBLE
			} else {
				holder.checkbox.visibility = View.VISIBLE
				holder.checkboxChecked.visibility = View.INVISIBLE
			}
			
			//deadline on click listener
			ButtonAnimator(holder.itemView).animateWeakPressing()
			holder.itemView.setOnClickListener {
				onDeadlineClicked(realmResults.realm.copyFromRealm(deadline))
			}
			
			//checkbox on click listener
			holder.checkbox.setOnClickListener {
				holder.checkbox.visibility = View.INVISIBLE
				holder.checkboxChecked.visibility = View.VISIBLE
				holder.statusNormal.visibility = View.INVISIBLE
				holder.statusClosed.visibility = View.VISIBLE
				holder.statusExpired.visibility = View.INVISIBLE
				onDeadlineCheckboxClicked(realmResults.realm.copyFromRealm(deadline))
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
				onDeadlineCheckboxClicked(realmResults.realm.copyFromRealm(deadline))
			}
		}
	}
}