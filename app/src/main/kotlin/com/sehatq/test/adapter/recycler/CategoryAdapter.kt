package com.sehatq.test.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.extension.android.view.inflate
import com.sehatq.test.R
import com.sehatq.test.adapter.recycler.core.DataListRecyclerViewAdapter
import com.sehatq.test.model.remote.CategoryProduct
import kotlinx.android.synthetic.main.adapter_recycler_category.view.img_category
import kotlinx.android.synthetic.main.adapter_recycler_category.view.txt_product_name

class CategoryAdapter : DataListRecyclerViewAdapter<CategoryProduct, CategoryAdapter.ViewHolder>() {

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_recycler_category))
    }

    override fun onBindDataViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindView() {
            val data = getDataList()[adapterPosition]
            itemView.img_category.setImageURI(data.imageUrl)
            itemView.txt_product_name.text = data.name
        }
    }
}