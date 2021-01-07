package com.sehatq.test.model.remote

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class ProductPromo {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("imageUrl")
    var imageUrl: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("loved")
    var loved: Int? = null


    class DiffUtilCallback(
            private val oldList: List<ProductPromo> = ArrayList(),
            private val newList: List<ProductPromo> = ArrayList()): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // TODO: Use your own implementation, this is just a sample
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // TODO: Use your own implementation, this is just a sample
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}