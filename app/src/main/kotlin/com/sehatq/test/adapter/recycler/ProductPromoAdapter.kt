package com.sehatq.test.adapter.recycler

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.extension.android.view.inflate
import com.sehatq.test.App.Companion.context
import com.sehatq.test.R
import com.sehatq.test.adapter.recycler.core.DataListRecyclerViewAdapter
import com.sehatq.test.model.remote.ProductPromo
import kotlinx.android.synthetic.main.adapter_recycler_product.view.img_product
import kotlinx.android.synthetic.main.adapter_recycler_product.view.txt_name_product
import kotlinx.android.synthetic.main.item_favorite.view.img_favorite

class ProductPromoAdapter : DataListRecyclerViewAdapter<ProductPromo, ProductPromoAdapter.ViewHolder>() {

    var onProductClick: OnProductClick? = null

    override fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.adapter_recycler_product))
    }

    override fun onBindDataViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bindView() {
            val data = getDataList()[adapterPosition]
            itemView.img_favorite.visibility = View.VISIBLE

            itemView.txt_name_product.text = data.title
            itemView.img_product.setImageURI(data.imageUrl)

            if (data.loved == 0)
                itemView.img_favorite.setImageDrawable(context.getDrawable(R.drawable.ic_heart_disable))
            else
                itemView.img_favorite.setImageDrawable(context.getDrawable(R.drawable.ic_heart))

            itemView.setOnClickListener { onProductClick?.invoke(data, adapterPosition) }
        }
    }
}

typealias OnProductClick = ((data: ProductPromo, position: Int) -> Unit)
