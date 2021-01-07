package com.sehatq.test.view.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessScrollListener(@param:RecyclerView.Orientation var direction: Int = RecyclerView.VERTICAL, var threshold: Int = 1) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        // Do nothing
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (direction == RecyclerView.VERTICAL && dy > 0 || direction == RecyclerView.HORIZONTAL && dx > 0) {
            recyclerView.layoutManager?.let { layoutManager ->
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                // TODO: Add additional support for other layout managers
                val pastVisibleItems = when (layoutManager) {
                    is LinearLayoutManager -> {
                        layoutManager.findFirstVisibleItemPosition()
                    }
                    is StaggeredGridLayoutManager -> {
                        var items: IntArray? = null
                        items = layoutManager.findFirstVisibleItemPositions(items)
                        if (items != null && items.isNotEmpty()) items[0]
                        else 0
                    }
                    else -> {
                        0
                    }
                }

                // Start fetching new data when n to last item is already visible
                if (visibleItemCount + pastVisibleItems >= totalItemCount - threshold) {
                    onLoadMore()
                }
            }
        }
    }

    /** Called when scroll reaches the threshold to obtain the next set of data  */
    abstract fun onLoadMore()
}
