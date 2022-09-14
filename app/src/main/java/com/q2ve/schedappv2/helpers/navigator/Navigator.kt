package com.q2ve.schedappv2.helpers.navigator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

/**
 * You must initialize Navigator before use it.
 * To initialize you need to use the "configure" method.
 */
object Navigator {
	private lateinit var fragmentManager: FragmentManager
	
	/**
	 * Configures Navigator with link to activity.
	 * It is necessary to Navigator can get transactions from activity's fragment manager.
	 */
	fun configure(fragmentManager: FragmentManager) {
		this.fragmentManager = fragmentManager
	}
	
	/**
	 * Removes all fragments from backstack. The name of each fragment should not be "clr_fragment".
	 */
	fun clearBackstack() {
		fragmentManager.popBackStack("clr_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
	}
	
	/**
	 * Removes fragment with selected animation and optional adding to backstack.
	 */
	fun removeFragment(
		fragment: Fragment,
		animation: ReplaceAnimation? = null,
		addToBackStack: Boolean = false
	) {
		val transaction = getTransaction()
		setAnimation(transaction, animation)
		transaction.remove(fragment)
		commitTransaction(transaction, addToBackStack)
	}
	
	/**
	 * Adds fragment with selected animation and optional adding to backstack.
	 */
	fun addFragment(
		fragment: Fragment,
		frame: Int,
		animation: ReplaceAnimation? = null,
		addToBackStack: Boolean = false
	) {
		val transaction = getTransaction()
		setAnimation(transaction, animation)
		transaction.add(frame, fragment)
		commitTransaction(transaction, addToBackStack)
	}
	
	/**
	 * Replaces fragment with selected fragment and animation and optional adding to backstack.
	 */
	fun replaceFragment(
		fragment: Fragment,
		frame: Int,
		animation: ReplaceAnimation? = null,
		addToBackStack: Boolean = false
	) {
		val transaction = getTransaction()
		setAnimation(transaction, animation)
		transaction.replace(frame, fragment)
		commitTransaction(transaction, addToBackStack)
	}
	
	private fun getTransaction(): FragmentTransaction {
		return fragmentManager.beginTransaction()
	}
	
	private fun setAnimation(transaction: FragmentTransaction, animation: ReplaceAnimation?) {
		if (animation != null) {
			transaction.setCustomAnimations(
				animation.enterAnimation,
				animation.exitAnimation,
				animation.popEnterAnimation,
				animation.popExitAnimation
			)
		}
	}
	
	private fun commitTransaction(transaction: FragmentTransaction, addToBackStack: Boolean) {
		if (addToBackStack) { transaction.addToBackStack(null) }
		transaction.commit()
	}
}