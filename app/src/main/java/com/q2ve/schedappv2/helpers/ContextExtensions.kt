package com.q2ve.schedappv2.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.q2ve.schedappv2.MainActivity

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

//Keyboard hiding functions

fun Fragment.hideKeyboard() {
	view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
	hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
	val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.allowBackButtonAction(condition: Boolean) {
	view?.let {
		(activity as? MainActivity)?.allowBackButtonAction(condition)
	}
}

fun MainActivity.allowBackButtonAction(condition: Boolean) {
	allowBackButtonAction = condition
}