package com.sehatq.test.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sehatq.test.R
import com.sehatq.test.activity.DashboardActivity
import com.sehatq.test.activity.DetailProductActivity
import com.sehatq.test.adapter.recycler.PurchaseProductHistoryAdapter
import com.sehatq.test.adapter.recycler.core.DataListRecyclerViewAdapter
import com.sehatq.test.fragment.core.DataListFragment
import com.sehatq.test.model.remote.ProductPromo
import com.sehatq.test.viewmodel.PurchaseHistoryListViewModel
import kotlinx.android.synthetic.main.fragment_search_product.recycler_search_product
import kotlinx.android.synthetic.main.include_toolbar_search.btn_back
import kotlinx.android.synthetic.main.include_toolbar_search.ed_autoComplete_search
import kotlinx.android.synthetic.main.include_toolbar_search.layout_search_product

class PurchaseHistoryProductFragment : DataListFragment() {

  override val viewRes = R.layout.fragment_purchase_history_product

  private val listProduct: ArrayList<ProductPromo> = ArrayList()

  private var adapterProduct = PurchaseProductHistoryAdapter()

  /** The view model for get claim fee data*/
  private val viewModel by lazy { ViewModelProviders.of(this).get(PurchaseHistoryListViewModel::class.java) }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    initViewModel()

    layout_search_product.visibility = View.VISIBLE

    ed_autoComplete_search.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // Do nothing
      }

      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // Do nothing
      }

      override fun afterTextChanged(s: Editable) {
        adapterProduct.setDataListFilter { it.title != null && it.title!!.contains(s.toString(), ignoreCase = true) }
      }
    })

    btn_back.setOnClickListener {
      DashboardActivity.launchIntent(requireContext())
    }
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_search_product
  }

  private fun initViewModel() {
    viewModel.resourceAny.observe(viewLifecycleOwner, Observer {
      updateResource(it)
    })

    viewModel.dataListProduct.observe(viewLifecycleOwner, Observer {
      listProduct.clear()
      listProduct.addAll(it)

      updateDataList(listProduct)
    })

    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapterProduct.emptyText = resources.getString(R.string.info_no_data)
    adapterProduct.onPurchaseProductClick = { data, _ ->
      DetailProductActivity.launchIntent(requireContext(), data)
    }

    adapterProduct.setDiffUtilNotifier { oldList, newList ->
      ProductPromo.DiffUtilCallback(oldList, newList)
    }

    return adapterProduct as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.VERTICAL, false)
  }

  override fun fetchData() {
    viewModel.fetchPurchasedHistoryData()
  }
}