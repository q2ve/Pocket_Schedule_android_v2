package com.q2ve.pocketschedule2.ui.core.deadlines.pages

import android.animation.LayoutTransition
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.q2ve.pocketschedule2.databinding.DeadlinesPagerViewHolderBinding

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class DeadlinesPagerAdapter(
	private val viewPager: ViewPager2
): RecyclerView.Adapter<DeadlinesPagerAdapter.DeadlinesItemHolder>() {
	var pages: List<DeadlinesPageBase> = emptyList()
	
	class DeadlinesItemHolder(
		val viewBinding: DeadlinesPagerViewHolderBinding
	): RecyclerView.ViewHolder(viewBinding.root)
	
	override fun onCreateViewHolder(container: ViewGroup, viewType: Int): DeadlinesItemHolder {
		val binding = DeadlinesPagerViewHolderBinding.inflate(
			LayoutInflater.from(container.context),
			container,
			false
		)
		//EnableTransitionType is necessary to xml "animateLayoutChanges" works with height changing
		val contentContainer = binding.deadlinesPagerViewHolderContainer
		contentContainer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
		return DeadlinesItemHolder(binding)
	}
	
	override fun onBindViewHolder(holder: DeadlinesItemHolder, position: Int) {
		val container = holder.viewBinding.deadlinesPagerViewHolderContainer
		if (pages.size > position) {
			container.removeAllViews()
			val inflater = LayoutInflater.from(container.context)
			container.addView(pages[position].getView(inflater, container, position, ::replaceView))
		}
	}
	
	override fun getItemCount() = pages.size
	
	private fun replaceView(position: Int) {
		try {
			val page = (viewPager.getChildAt(0) as RecyclerView).getChildAt(position)
			ViewCompat.animate(page)
				.alpha(0f)
				.setInterpolator(AccelerateDecelerateInterpolator())
				.setDuration(300)
				.withEndAction {
					notifyItemChanged(position)
					ViewCompat.animate(page)
						.alpha(1f)
						.setInterpolator(AccelerateDecelerateInterpolator())
						.setStartDelay(100)
						.setDuration(300)
						.start()
				}
				.start()
		} catch (e: Throwable) {
			Log.e("DeadlinesPagerAdapter", "Probably missing viewPager", e)
		}
	}
}