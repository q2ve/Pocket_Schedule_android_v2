package com.q2ve.pocketschedule2.ui.bottomSheet

interface BottomSheetContract {
	interface View {
		var onClosedCallback: (() -> Unit)?
		var titleStringResource: Int?
		
		fun inflateRootLayoutWith(view: android.view.View)
		fun showBottomSheet()
		fun collapseBottomSheet()
	}
}