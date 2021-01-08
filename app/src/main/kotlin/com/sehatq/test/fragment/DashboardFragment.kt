package com.sehatq.test.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sehatq.test.R
import com.sehatq.test.activity.DetailProductActivity
import com.sehatq.test.activity.SearchProductActivity
import com.sehatq.test.adapter.recycler.CategoryAdapter
import com.sehatq.test.adapter.recycler.ProductPromoAdapter
import com.sehatq.test.adapter.recycler.core.DataListRecyclerViewAdapter
import com.sehatq.test.fragment.core.DataListFragment
import com.sehatq.test.model.remote.CategoryProduct
import com.sehatq.test.model.remote.ProductPromo
import com.sehatq.test.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_dashboard_product.recycler_category_product
import kotlinx.android.synthetic.main.fragment_dashboard_product.recycler_product
import kotlinx.android.synthetic.main.include_toolbar_search.ed_search_product
import kotlinx.android.synthetic.main.include_toolbar_search.layout_search_home

class DashboardFragment : DataListFragment() {

  override val viewRes = R.layout.fragment_dashboard_product

  private val listCategoryProduct: ArrayList<CategoryProduct> = ArrayList()

  private val listProduct: ArrayList<ProductPromo> = ArrayList()

  private val adapterCategory = CategoryAdapter()

  private var adapterProduct = ProductPromoAdapter()

  /** The view model for get claim fee data*/
  private val viewModel by lazy { ViewModelProviders.of(this).get(ProductListViewModel::class.java) }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    layout_search_home.visibility = View.VISIBLE
    initViewModel()

    ed_search_product.setOnClickListener{
      SearchProductActivity.launchIntent(requireContext())
    }
  }

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      updateResource(it)
    })
    viewModel.dataListCategory.observe(viewLifecycleOwner, Observer {
      listCategoryProduct.clear()
      listCategoryProduct.addAll(it)
      updateDataList(listCategoryProduct)
    })

    viewModel.dataListProduct.observe(viewLifecycleOwner, Observer {
      listProduct.clear()
      listProduct.addAll(it)

      initProductList(listProduct)
    })

    lifecycle.addObserver(viewModel)
  }

  override fun initRecyclerView(): RecyclerView {
    return recycler_category_product
  }

  override fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, ViewHolder> {
    adapterCategory.emptyText = resources.getString(R.string.info_no_data)
//    adapterCategory.onCategoryClick = { data ->
//      DetailClaimFeeActivity.launchIntent(context!!, data.trackingId!!)
//    }

    adapterCategory.setDiffUtilNotifier { oldList, newList ->
      CategoryProduct.DiffUtilCallback(oldList, newList)
    }

    return adapterCategory as DataListRecyclerViewAdapter<Any, ViewHolder>
  }

  override fun initRecyclerLayoutManager(): LayoutManager {
    return LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
  }

  override fun fetchData() {
    viewModel.fetchData(false)
  }

  private fun initProductList(dataProductList: ArrayList<ProductPromo>) {
    recycler_product.hasFixedSize()

    val layoutManagerFreshCategory: LayoutManager =
      LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    recycler_product.layoutManager = layoutManagerFreshCategory

    adapterProduct.setDataList(dataProductList)

    adapterProduct.onProductClick = { data, _ ->
      DetailProductActivity.launchIntent(requireContext(), data)
    }

    recycler_product.adapter = adapterProduct
  }
}