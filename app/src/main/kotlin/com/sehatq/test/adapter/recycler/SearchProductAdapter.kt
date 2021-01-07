package com.sehatq.test.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.extension.android.view.inflate
import com.sehatq.test.R
import com.sehatq.test.adapter.recycler.core.DataListRecyclerViewAdapter
import com.sehatq.test.model.remote.ProductPromo
import kotlinx.android.synthetic.main.adapter_recycler_product.view.txt_name_product
import kotlinx.android.synthetic.main.adapter_recycler_search_product.view.img_search_product
import kotlinx.android.synthetic.main.adapter_recycler_search_product.view.txt_price

class SearchProductAdapter : DataListRecyclerViewAdapter<ProductPromo, SearchProductAdapter.ViewHolder>() {

    var onSearchProductClick: OnSearchProductClick? = null

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_recycler_search_product))
    }

    override fun onBindDataViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val data = getDataList()[adapterPosition]
            itemView.txt_price.text = data.price
            itemView.txt_name_product.text = data.title
            itemView.img_search_product.setImageURI(data.imageUrl)

            itemView.setOnClickListener { onSearchProductClick?.invoke(data, adapterPosition) }
        }
    }
}

typealias OnSearchProductClick = ((data: ProductPromo, position: Int) -> Unit)
