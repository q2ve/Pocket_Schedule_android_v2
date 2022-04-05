package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.DeadlinesModuleBinding
import com.q2ve.pocketschedule2.helpers.UserObserver
import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline
import com.q2ve.pocketschedule2.ui.core.deadlines.cards.DeadlinesCard
import io.realm.RealmResults

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

abstract class DeadlinesPageModuleBase(private val emptyLayoutId: Int): DeadlinesPageBase() {
	private var router = DeadlinesPageModuleBaseRouter()
	private lateinit var inflater: LayoutInflater
	private lateinit var container: ViewGroup
	private lateinit var viewUpdatingCall: (Int) -> Unit
	private var position: Int? = null
	private var savedRecycler: RecyclerView? = null
	private var view: View? = null
	
	override fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		position: Int,
		viewUpdatingCall: (Int) -> Unit
	): View {
		this.inflater = inflater
		this.container = container
		this.viewUpdatingCall = viewUpdatingCall
		this.position = position
		
		view?.let { return it } ?: run {
			val dummy = inflater.inflate(R.layout.deadlines_loader_dummy, container, false)
			view = dummy
			getDeadlines()
			return dummy
		}
	}
	
	abstract fun getDeadlines()
	
	protected fun inflateDeadlines(deadlines: RealmResults<RealmItemDeadline>?) {
		if (deadlines == null) view = inflater.inflate(emptyLayoutId, container, false)
		else {
			val binding = DeadlinesModuleBinding.inflate(inflater, container, false)
			val recyclerView = binding.coreDeadlinesModuleRecycler
			val layoutManager = LinearLayoutManager(inflater.context)
			recyclerView.layoutManager = layoutManager
			
			fun showDummy() {
				Log.e("TEST", "showDummy")
				view = inflater.inflate(emptyLayoutId, container, false)
				position?.let { viewUpdatingCall(it) }
			}
			fun hideDummy() {
				Log.e("TEST", "hideDummy")
				savedRecycler?.let { recycler ->
					view = recycler
					position?.let { viewUpdatingCall(it) }
				}
			}
			
			recyclerView.adapter = DeadlinesPageModuleAdapter(
				::onDeadlineClicked,
				::onDeadlineCheckboxClicked,
				deadlines,
				::showDummy,
				::hideDummy
			)
			view = binding.root
			savedRecycler = binding.root
		}
		position?.let { viewUpdatingCall(it) }
	}
	
	private fun placeDeadlineCard(deadline: RealmItemDeadline) {
		val sessionId = UserObserver.getMainObject().sessionId
		if (sessionId == null) router.openAuthorizationRequirement() else {
			router.openBottomPopupContainer(R.string.deadline_card) {
				fun closeBottomPopupContainer() { it.animateExit() }
				
				val inflater = it.layoutInflater
				val container = it.binding.bottomPopupContainerContentContainer
				val resources = it.resources
				val theme = it.requireActivity().theme
				val card = DeadlinesCard(sessionId, deadline, { closeBottomPopupContainer() }).getView(
					inflater, container, resources, theme
				)
				container.addView(card)
			}
		}
	}
	
	private fun onDeadlineClicked(deadline: RealmItemDeadline) {
		placeDeadlineCard(deadline)
	}
	
	private fun onDeadlineCheckboxClicked(deadline: RealmItemDeadline) {
		val sessionId = UserObserver.getMainObject().sessionId
		if (sessionId == null) router.openAuthorizationRequirement() else {
			when (deadline.isClosed) {
				true -> {
					Model{ }.openDeadline(sessionId, deadline._id) { }
				}
				false -> {
					Model{ }.closeDeadline(sessionId, deadline._id) { }
				}
			}
		}
	}
}