package com.q2ve.pocketschedule2.helpers.navigator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.q2ve.pocketschedule2.R

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

/**
 * You must initialize Navigator before use it.
 * To initialize you need to use the "configure" method.
 */
object Navigator {
	private lateinit var activityLink: FragmentActivity
	
	/**
	 * Configures Navigator with link to activity.
	 * It is necessary to Navigator can get transactions from activity's fragment manager.
	 */
	fun configure(activity: FragmentActivity) {
		activityLink = activity
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
		return activityLink.supportFragmentManager.beginTransaction()
	}
	
	private fun setAnimation(transaction: FragmentTransaction, animation: ReplaceAnimation?) {
		//TODO("Проверить, нужно ли возвращать транзакцию. Если да - добавить ретурн.")
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