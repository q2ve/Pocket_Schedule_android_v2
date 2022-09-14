package com.q2ve.schedappv2.helpers

/**
 * Created by Denis Shishkin
 * qwq2eq@gmail.com
 */

private typealias Observer<T> = (T) -> Unit

class Observable<T>(value: T) {
	private val callbacks = mutableListOf<Observer<T>>()
	
	var value: T = value
	set(value) {
		field = value
		callbacks.forEach { it(value) }
	}
	
	fun subscribe(observer: Observer<T>) {
		callbacks.add(observer)
	}
	
	fun unsubscribe(observer: Observer<T>) {
		callbacks.remove(observer)
	}
	
	fun unsubscribeAll() {
		callbacks.clear()
	}
}