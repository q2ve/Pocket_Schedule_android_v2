package com.q2ve.pocketschedule2.helpers.recyclerSelector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.ui.ButtonAnimator

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class RecyclerSelectorAdapter(
	var stringsList: List<String>,
	private val callbackOnItemClicked: (Int) -> Unit
): RecyclerView.Adapter<RecyclerSelectorAdapter.RecyclerItemHolder>() {

	class RecyclerItemHolder(item: View): RecyclerView.ViewHolder(item) {
		var name: TextView = item.findViewById(R.id.recycler_item_text)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemHolder {
		val itemView = LayoutInflater
						.from(parent.context)
						.inflate(R.layout.recycler_item, parent, false)
		return RecyclerItemHolder(itemView)
	}

	override fun onBindViewHolder(holder: RecyclerItemHolder, position: Int) {
		var text = stringsList[position]
		
		//TODO("Проверить, нужно ли еще это обрезание.")
		if (text.contains("—")) {
			(text).forEachIndexed { pos, it ->
				if (it.toString() == "—") {
					text = text.dropLast(text.length - (pos - 1))
				}
			}
		}
		holder.name.text = text
		
		holder.itemView.setOnClickListener { callbackOnItemClicked(position) }
		ButtonAnimator(holder.itemView).animateWeakPressing()
	}

	override fun getItemCount() = stringsList.size
}