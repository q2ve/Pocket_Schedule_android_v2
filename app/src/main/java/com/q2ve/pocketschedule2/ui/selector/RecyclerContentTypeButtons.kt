package com.q2ve.pocketschedule2.ui.selector
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import com.q2ve.pocketschedule2.R
//
///**
// * Created by Denis Shishkin
// * qwq2eq@gmail.com
// */
//
//interface RecyclerContentTypeButtonsInterface {
//	fun contentTypeButtonsCallback(newContentType: IndexerItemType)
//}
//
//class RecyclerContentTypeButtons(private val parent: RecyclerContentTypeButtonsInterface): Fragment() {
//	private var selectedContentType = IndexerItemType.Groups
//
//	override fun onCreateView(
//		inflater: LayoutInflater,
//		container: ViewGroup?,
//		savedInstanceState: Bundle?
//	): View? {
//		val rootView = inflater.inflate(R.layout.recycler_content_type_buttons, container, false)
//
//		val groupsButton: TextView = rootView.recycler_view_selector_groups_button
//		val professorsButton: TextView = rootView.recycler_view_selector_professors_button
//
//		fun dyeTheButton(button: TextView) {
//			button.setTextColor(resources.getColor(R.color.colorBlue, activity!!.theme))
//			button.background.setTint(resources.getColor(R.color.colorBlueLight, activity!!.theme))
//		}
//
//		fun undyeTheButton(button: TextView) {
//			button.setTextColor(resources.getColor(R.color.colorLightGray1, activity!!.theme))
//			button.background.setTint(resources.getColor(R.color.colorPlaqueBackground, activity!!.theme))
//		}
//
//		fun undyeAllButtons() {
//			undyeTheButton(groupsButton)
//			undyeTheButton(professorsButton)
//		}
//
//		undyeAllButtons()
//		dyeTheButton(groupsButton)
//
//		groupsButton.setOnClickListener {
//			if (selectedContentType != IndexerItemType.Groups) {
//				selectedContentType = IndexerItemType.Groups
//				undyeAllButtons()
//				dyeTheButton(groupsButton)
//				parent.contentTypeButtonsCallback(selectedContentType)
//			}
//		}
//		professorsButton.setOnClickListener {
//			if (selectedContentType != IndexerItemType.Professors) {
//				selectedContentType = IndexerItemType.Professors
//				undyeAllButtons()
//				dyeTheButton(professorsButton)
//				parent.contentTypeButtonsCallback(selectedContentType)
//			}
//		}
//
//		return rootView
//	}
//}