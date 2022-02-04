package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.DeadlinesModuleBinding
import com.q2ve.pocketschedule2.model.dataclasses.RealmItemDeadline

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

abstract class DeadlinesPageModuleBase(
	private val emptyLayoutId: Int,
	private val onDeadlineClicked: (RealmItemDeadline) -> Unit,
	private val onDeadlineCheckboxClicked: (RealmItemDeadline) -> Unit
): DeadlinesPageBase() {
	private lateinit var inflater: LayoutInflater
	private lateinit var container: ViewGroup
	private lateinit var viewUpdatingFunction: (Int) -> Unit
	private var position: Int? = null
	private var view: View? = null
	
	override fun getView(
		inflater: LayoutInflater,
		container: ViewGroup,
		position: Int,
		viewUpdatingFunction: (Int) -> Unit
	): View {
		this.inflater = inflater
		this.container = container
		this.viewUpdatingFunction = viewUpdatingFunction
		this.position = position
		
		view?.let { return it } ?: run {
			val dummy = inflater.inflate(R.layout.deadlines_loader_dummy, container, false)
			view = dummy
			getDeadlines()
			return dummy
		}
	}
	
	abstract fun getDeadlines()
	
	protected fun inflateDeadlines(deadlines: List<RealmItemDeadline>) {
		if (deadlines.isEmpty()) {
			view = inflater.inflate(emptyLayoutId, container, false)
		} else {
			val binding = DeadlinesModuleBinding.inflate(inflater, container, false)
			val recyclerView = binding.coreDeadlinesModuleRecycler
			val layoutManager = LinearLayoutManager(inflater.context)
			recyclerView.layoutManager = layoutManager
			recyclerView.adapter = DeadlinesPageModuleAdapter(
				onDeadlineClicked,
				onDeadlineCheckboxClicked,
				deadlines
			)
			view = binding.root
		}
		position?.let { viewUpdatingFunction(it) }
	}
}