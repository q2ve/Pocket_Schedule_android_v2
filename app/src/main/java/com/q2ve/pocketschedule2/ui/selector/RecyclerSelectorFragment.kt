package com.q2ve.pocketschedule2.ui.selector
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.q2ve.pocketschedule2.databinding.RecyclerSelectorBinding
//import com.q2ve.pocketschedule2.helpers.hideKeyboard
//import com.q2ve.pocketschedule2.helpers.recyclerSelector.RecyclerSelectorAdapter
//import com.q2ve.pocketschedule2.helpers.recyclerSelector.RecyclerSelectorOnScrollListener
//import com.q2ve.pocketschedule2.model.realm.RealmNameInterface
//
///**
// * Created by Denis Shishkin
// * qwq2eq@gmail.com
// */
//
//interface RecyclerFragmentInterface {
//	fun onRecyclerItemClicked(realmObject: RealmNameInterface)
//	fun searchItems(name: String)
//	fun uploadMoreItems()
//}
//
////TODO(There must be delay after user input in search box)
//class RecyclerSelectorFragment(
//	private val parent: RecyclerFragmentInterface,
//	private val inputData: List<RealmNameInterface>,
//	private val hideSearchField: Boolean = false
//): Fragment() {
//	private val viewModel = ViewModelProvider(this).get(RecyclerSelectorViewModel::class.java)
//
//	private lateinit var binding: RecyclerSelectorBinding
//	private lateinit var errorViewer: TextView
//
//	private val recyclerAdapter = RecyclerSelectorAdapter(inputData, parent)
//	private var allowAddingItems = true
//	private var previousItemCount = 20
//	private var isGroupsSelected = true
//
//	override fun onCreateView(
//	inflater: LayoutInflater,
//	container: ViewGroup?,
//	savedInstanceState: Bundle?
//	): View {
//		binding = RecyclerSelectorBinding.inflate(inflater, container, false)
//
//		//Initializing RecyclerView
//		initRecycler()
//
//		//Initializing search field, adding change listener
//		val searchField = binding.recyclerViewSelectorSearchField
//		val cancelButton = binding.recyclerViewSelectorCancelButton
//		searchField.addTextChangedListener(object: TextWatcher {
//			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
//
//			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//				if (s != null) {
//					Log.d("s", s.toString())
//					parent.searchItems(s.toString())
//					cancelButton.isVisible = true
//					cancelButton.setOnClickListener { searchField.editableText.clear() }
//					if (s.isEmpty()) { cancelButton.isVisible = false }
//				}
//			}
//			override fun afterTextChanged(s: Editable?) { }
//		})
//		if (hideSearchField) {
//			binding.recyclerViewSelectorSearchBox.visibility = View.GONE
//		}
//		searchField.setOnFocusChangeListener { _, hasFocus ->
//			if (!hasFocus) hideKeyboard()
//		}
//
//		//Initializing errorViewer
//		errorViewer = binding.recyclerErrorTextview
//
//		return binding.root
//	}
//
//	private fun initRecycler () {
//		val recyclerView: RecyclerView = binding.recyclerRecyclerview
//		val layoutManager = LinearLayoutManager(activity)
//		recyclerView.layoutManager = layoutManager
//		recyclerView.adapter = recyclerAdapter
//		recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//				super.onScrolled(recyclerView, dx, dy)
//				val itemCount = recyclerView.adapter!!.itemCount
//				if (layoutManager.findLastVisibleItemPosition() >= itemCount - 5) {
//					if (allowAddingItems) {
//						binding.recyclerUploadingProgressbar.visibility = View.VISIBLE
//						parent.uploadMoreItems()
//						allowAddingItems = false
//					}
//					if (itemCount > previousItemCount)	{
//						allowAddingItems = true
//					}
//					previousItemCount = itemCount
//				}
//			}
//		})
//		recyclerView.addOnScrollListener(
//			RecyclerSelectorOnScrollListener(
//			layoutManager,
//			parent::uploadMoreItems
//		)
//		)
//	}
//
//	fun updateData (data: List<RealmNameInterface>) {
//		allowAddingItems = false
//		recyclerAdapter.stringsList = data
//		recyclerAdapter.notifyDataSetChanged()
//	}
//
//	fun addItems (data: List<RealmNameInterface>) {
//		allowAddingItems = false
//		binding.recyclerUploadingProgressbar.visibility = View.GONE
//		recyclerAdapter.stringsList += data
//		recyclerAdapter.notifyDataSetChanged()
//	}
//
//	fun makeErrorMessage(stringResource: Int) {
//		errorViewer.text = getString(stringResource)
//	}
//
//	fun clearErrorMessage() {
//		errorViewer.text = ""
//	}
//}