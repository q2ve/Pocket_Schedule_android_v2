package com.q2ve.pocketschedule2.ui.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.q2ve.pocketschedule2.R
import com.q2ve.pocketschedule2.databinding.PopupMessageSmallBinding

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

class PopupMessageSmallFragment: Fragment() {
	companion object {
		fun newInstance(isError: Boolean, stringResource: Int, ttl: Long): PopupMessageSmallFragment {
			val fragment = PopupMessageSmallFragment()
			fragment.arguments = bundleOf(
				"stringResource" to stringResource,
				"isError" to isError,
				"ttl" to ttl,
			)
			return fragment
		}
	}
	
	var onViewCreatedCallback: ((Fragment, Long) -> Unit)? = null
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = PopupMessageSmallBinding.inflate(inflater, container, false)
		
		//TODO("ButtonAnimator(binding.root).animateJiggle")
		
		val stringResource = arguments?.getInt("stringResource")
		val textView = binding.popupMessageSmallText
		textView.text = getString(stringResource ?: R.string.an_error_has_occurred_try_again)
		
		val isError = arguments?.getBoolean("isError") ?: true
		val icon = binding.popupMessageSmallIcon
		if (isError) {
			icon.setImageDrawable(resources.getDrawable(R.drawable.ic_cancel_cross_inline))
			icon.drawable.setTint(resources.getColor(R.color.colorRedError))
		}
		else {
			//TODO("Other icon. And refactor this. Fragment should receive only text and icons`s id.")
			icon.setImageDrawable(resources.getDrawable(R.drawable.ic_view))
			icon.drawable.setTint(resources.getColor(R.color.colorBlue))
		}
		
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val ttl = arguments?.getLong("ttl") ?: 4000
		onViewCreatedCallback?.invoke(this, ttl)
	}
}