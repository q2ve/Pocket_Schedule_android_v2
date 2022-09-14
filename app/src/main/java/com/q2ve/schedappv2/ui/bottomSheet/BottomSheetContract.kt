package com.q2ve.schedappv2.ui.bottomSheet

interface BottomSheetContract {
	interface View {
		var onClosedCallback: (() -> Unit)?
		
		fun setTitle(titleStringId: Int?)
		fun inflate(view: android.view.View)
		fun collapseBottomSheet()
	}
}