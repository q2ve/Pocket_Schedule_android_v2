package com.q2ve.pocketschedule2.ui.core.deadlines

class DeadlinesViewModelExtensions {
	/*fun filterDeadlines(input: List<RealmItemDeadline>): DeadlinesSortedLists {
		val currentTime = (System.currentTimeMillis()/1000).toInt()
		val output = DeadlinesSortedLists()
		input.forEach {
			val isExternal = it.isExternal == true
			val isOpened = it.isClosed == false && it.isExternal == false && it.endDate?: 0 > currentTime
			val isClosed = it.isClosed == true && it.isExternal == false
			val isExpired = it.endDate?: 0 < currentTime && it.isClosed == false && it.isExternal == false
			when {
				isExternal -> output.externalDeadlines += it
				isOpened -> output.openedDeadlines += it
				isClosed -> output.closedDeadlines += it
				isExpired -> output.expiredDeadlines += it
			}
		}
		
		return output
	}
	
	fun filterExternalDeadlines(input: List<RealmItemDeadline>, ) {
	
	}*/
}