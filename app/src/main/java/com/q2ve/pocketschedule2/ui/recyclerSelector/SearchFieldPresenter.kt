package com.q2ve.pocketschedule2.ui.recyclerSelector

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.q2ve.pocketschedule2.databinding.RecyclerSearchBoxBinding

class SearchFieldPresenter {
	fun placeSearchField(
		container: ViewGroup,
		inflater: LayoutInflater,
		onSearchCallback: (String) -> Unit,
		onSearchFieldLostFocusCallback: () -> Unit,
		searchThrottlingTimeout: Long = 300
	) {
		val searchBox = RecyclerSearchBoxBinding.inflate(
			inflater,
			container,
			false
		)
		
		val searchField = searchBox.recyclerSelectorSearchField
		val cancelButton = searchBox.recyclerSelectorCancelButton
		
		var throttledText: String
		fun sendThrottledCallback(text: String) {
			throttledText = text
			Handler().postDelayed({
				if (throttledText == text) onSearchCallback(text)
			}, searchThrottlingTimeout)
		}
		
		searchField.addTextChangedListener(object: TextWatcher {
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				if (s != null) {
					sendThrottledCallback(s.toString())
					cancelButton.isVisible = true
					cancelButton.setOnClickListener { searchField.editableText.clear() }
					if (s.isEmpty()) cancelButton.isVisible = false
				}
			}
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
			override fun afterTextChanged(s: Editable?) { }
		})
		
		searchField.setOnFocusChangeListener { _, hasFocus ->
			if (!hasFocus) onSearchFieldLostFocusCallback()
		}
		
		container.addView(searchBox.root)
	}
}