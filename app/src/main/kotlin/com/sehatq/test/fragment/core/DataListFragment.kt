package com.sehatq.test.fragment.core

import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.appbar.AppBarLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.test.R
import com.sehatq.test.adapter.recycler.core.DataListRecyclerViewAdapter
import com.sehatq.test.model.core.AppError
import com.sehatq.test.model.core.Resource
import com.sehatq.test.view.recyclerview.EndlessScrollListener
import kotlinx.android.synthetic.main.base_fragment_data_list.*

/**
 * A fragment that can easily display data in a [RecyclerView].
 * Also capable of handling **vertical** infinite scrolling behaviour,
 * and calculates app bar offset to determine swipe-to-refresh behaviour.
 */
abstract class DataListFragment : CoreFragment(), AppBarLayout.OnOffsetChangedListener {

    override val viewRes: Int? = R.layout.base_fragment_data_list

    /** The adapter for the [RecyclerView] */
    private lateinit var adapterImpl: DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>

    /** The [RecyclerView] */
    private lateinit var recyclerViewImpl: RecyclerView

    /** The [RecyclerView.LayoutManager] for the [RecyclerView] */
    private lateinit var layoutManagerImpl: RecyclerView.LayoutManager

    /** The empty [RecyclerView.LayoutManager] for the [RecyclerView] */
    private lateinit var emptyLayoutManagerImpl: RecyclerView.LayoutManager

    /** The [AppBarLayout] */
    private var appBarLayoutImpl: AppBarLayout? = null

    /** The [SwipeRefreshLayout] */
    private var swipeRefreshLayoutImpl: SwipeRefreshLayout? = null

    /** Swipe-to-refresh behaviour */
    private var swipeToRefresh = false

    /** Infinite scrolling behaviour */
    private var infiniteScrolling = false

    /** Determines if this [DataListFragment] is currently doing a loading process */
    private var isLoading = false

    /** The value of this [DataListFragment] app bar vertical offset */
    private var appBarVerticalOffset = -1

    /** The recycler scroll state */
    private var savedRecyclerState: Parcelable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAbstracts()
        initView()

        if (savedInstanceState == null) {
            initData()
        } else {
            savedRecyclerState = savedInstanceState.getParcelable(RECYCLER_SCROLL_STATE)
            adapterImpl.isRestoringRecyclerState = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(RECYCLER_SCROLL_STATE, layoutManagerImpl.onSaveInstanceState())
    }

    /** Sets the data from some abstract methods  */
    private fun initAbstracts() {
        adapterImpl = initRecyclerAdapter()
        recyclerViewImpl = initRecyclerView()
        layoutManagerImpl = initRecyclerLayoutManager()
        emptyLayoutManagerImpl = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        appBarLayoutImpl = initAppBarLayout()
        swipeRefreshLayoutImpl = initSwipeRefreshLayout()
    }

    /** Initializes the view for this fragment  */
    private fun initView() {
        initAppBarLayoutImpl()
        initSwipeRefreshLayoutImpl()
        initRecyclerViewImpl()
    }

    /** Initializes some app bar layout behaviour  */
    private fun initAppBarLayoutImpl() {
        appBarLayoutImpl?.addOnOffsetChangedListener(this)
    }

    /** Initialize some customization for the swipe refresh layout  */
    private fun initSwipeRefreshLayoutImpl() {
        if (swipeRefreshLayoutImpl != null) {
            swipeRefreshLayoutImpl!!.setColorSchemeResources(R.color.accent)
            swipeRefreshLayoutImpl!!.isEnabled = false
            swipeRefreshLayoutImpl!!.setOnRefreshListener {
                onSwipeToRefresh()
            }
        }
    }

    /**
     * Initializes the recycler view, its adapter, and its layout manager.
     * Also adds a scroll listener to handle infinite scrolling behaviour.
     */
    private fun initRecyclerViewImpl() {
        adapterImpl.setOnRetryListener { onRetryClicked() }
        recyclerViewImpl.layoutManager = emptyLayoutManagerImpl
        recyclerViewImpl.adapter = adapterImpl

        // Add a scroll listener
        recyclerViewImpl.addOnScrollListener(object : EndlessScrollListener(RecyclerView.VERTICAL, 3) {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> onOffsetChanged(appBarLayoutImpl, appBarVerticalOffset)
                }
            }

            override fun onLoadMore() {
                if (infiniteScrolling && !isLoading && adapterImpl.getState() == DataListRecyclerViewAdapter.NORMAL) fetchData()
            }
        })
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (swipeRefreshLayoutImpl == null || !swipeToRefresh) return
        appBarVerticalOffset = verticalOffset

        if (!isLoading) {
            if (appBarLayout != null) {
                swipeRefreshLayoutImpl!!.isEnabled = (verticalOffset == 0 && recyclerViewImpl.computeVerticalScrollOffset() == 0)
            } else {
                swipeRefreshLayoutImpl!!.isEnabled = true
            }
        }
    }

    /** Initializes data load */
    private fun initData() {
        if (adapterImpl.getDataList().isEmpty()) fetchData()
        else onLoadSuccessImpl()
    }

    /** Clears the data list */
    fun clearData() {
        adapterImpl.setDataList(null) { updateLayoutManager() }
    }

    /**
     * Obtain the [RecyclerView] adapter.
     * @return the adapter
     */
    fun getAdapter(): DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder> {
        return adapterImpl
    }

    /**
     * Obtain the [RecyclerView] view.
     * @return the [RecyclerView]
     */
    fun getRecyclerView(): RecyclerView {
        return recyclerViewImpl
    }

    /**
     * Obtain the [RecyclerView] layout manager.
     * @return the layout manager
     */
    fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManagerImpl
    }

    /**
     * Obtain the [AppBarLayout] view.
     * @return the [AppBarLayout]
     */
    fun getAppBarLayout(): AppBarLayout? {
        return appBarLayoutImpl
    }

    /**
     * Obtain the [SwipeRefreshLayout] view.
     * @return the [SwipeRefreshLayout]
     */
    fun getSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayoutImpl
    }

    /** Enables swipe-to-refresh layout behaviour */
    fun enableSwipeToRefresh() {
        swipeToRefresh = true
        if (!isLoading) {
            if (appBarLayoutImpl != null) {
                swipeRefreshLayoutImpl?.isEnabled = (appBarVerticalOffset == 0 && recyclerViewImpl.computeVerticalScrollOffset() == 0)
            } else {
                swipeRefreshLayoutImpl?.isEnabled = true
            }
        }
    }

    /** Disables swipe-to-refresh layout behaviour */
    fun disableSwipeToRefresh() {
        swipeToRefresh = false
        swipeRefreshLayoutImpl?.isEnabled = true
    }

    /** Enables infinite scrolling layout behaviour */
    fun enableInfiniteScrolling() {
        infiniteScrolling = true
    }

    /** Disables infinite scrolling layout behaviour */
    fun disableInfiniteScrolling() {
        infiniteScrolling = false
    }

    /**
     * Updates the resource.
     * @param resource the [Resource] object
     */
    @Suppress("UNCHECKED_CAST")
    fun updateResource(resource: Resource<*>?) {
        if (resource == null) return
        adapterImpl.setState(resource)

        when (resource.status) {
            Resource.LOADING -> onLoading()
            Resource.SUCCESS -> onSuccess()
            Resource.ERROR -> onError(resource.error)
        }
    }

    /**
     * Updates the data list.
     * @param collection the [Collection]
     */
    fun updateDataList(collection: Collection<Any>?) {
        adapterImpl.setDataList(collection, ({ this.updateLayoutManager() }))
    }

    /**
     * Updates the layout manager.
     * @param newLayoutManager the new layout manager
     */
    fun updateLayoutManager(newLayoutManager: RecyclerView.LayoutManager = layoutManagerImpl) {
        layoutManagerImpl = newLayoutManager
        if (adapterImpl.getDataList().size > 0) {
            if (recyclerViewImpl.layoutManager !== layoutManagerImpl) {
                recyclerViewImpl.layoutManager = layoutManagerImpl
                restoreScrollStateIfNeeded()
            }
        } else {
            recyclerViewImpl.layoutManager = emptyLayoutManagerImpl
        }
    }

    /** Try to restore the saved scroll state if required  */
    private fun restoreScrollStateIfNeeded() {
        if (savedRecyclerState != null) {
            layoutManagerImpl.onRestoreInstanceState(savedRecyclerState)
            adapterImpl.isRestoringRecyclerState = false
            savedRecyclerState = null
        }
    }

    /** Updates the adapter to display loading progress */
    private fun onLoading() {
        isLoading = true
        if (swipeRefreshLayoutImpl != null && !swipeRefreshLayoutImpl!!.isRefreshing) {
            swipeRefreshLayoutImpl!!.isEnabled = false
            swipeRefreshLayoutImpl!!.isRefreshing = false
        }

        updateLayoutManager()
        onLoadStart(adapterImpl.getDataList().size > 0)
    }

    /**
     * Called when a loading process is starting.
     * Override this method if further customization is required.
     */
    open fun onLoadStart(isPaginatedLoad : Boolean) {
        // Stub!
    }

    /** Hides the loading progress and re-enable swipe refresh layout if required */
    private fun onSuccess() {
        if (swipeRefreshLayoutImpl != null) {
            swipeRefreshLayoutImpl!!.isEnabled = false
            swipeRefreshLayoutImpl!!.isRefreshing = false
        }

        updateLayoutManager()
        onLoadSuccessImpl()

        // Check if RecyclerView is already at the bottom.
        // If it's currently at the bottom, call API to obtain the next page's data.
        // Delay it by 500ms to wait until adapter has been notified,
        // too bad there aren't any reliable way to wait until notify has been finished.
        if (infiniteScrolling) {
            recyclerViewImpl.postDelayed({
                if (activity != null && activity!!.isFinishing) return@postDelayed
                if (recyclerViewImpl.canScrollVertically(1)) return@postDelayed
                if (infiniteScrolling && !isLoading && adapterImpl.getState() == DataListRecyclerViewAdapter.NORMAL) fetchData()
            }, 1000)
        }
    }

    /** Sets this fragment state to not loading anymore */
    fun onLoadSuccessImpl() {
        onLoadSuccess(adapterImpl.getDataList().size > 0)
        isLoading = false
    }

    /**
     * Called when fetching data process is completed successfully.
     * Override this method if further customization is required.
     */
    open fun onLoadSuccess(isPaginatedLoad : Boolean) {
        // Stub!
    }

    /**
     * Updates the adapter to display error.
     * @param error the error
     */
    private fun onError(error: AppError) {
        adapterImpl.errorText = error.message
        if (swipeRefreshLayoutImpl != null) {
            swipeRefreshLayoutImpl!!.isEnabled = false
            swipeRefreshLayoutImpl!!.isRefreshing = false
        }

        updateLayoutManager()
        onLoadFailure(error, adapterImpl.getDataList().size > 0)
        isLoading = false
    }

    /**
     * Called when fetching data process has failed.
     * Override this method if further customization is required.
     */
    open fun onLoadFailure(error: AppError, isPaginatedLoad : Boolean) {
        // Stub!
    }

    /**
     * Called when a swipe-to-refresh is performed.
     * Override this method to customize its implementation.
     */
    open fun onSwipeToRefresh() {
        // Stub!
    }

    /**
     * Called when the retry button on error layout is performed.
     * Override this method to customize its implementation.
     */
    open fun onRetryClicked() {
        fetchData()
    }

    /**
     * Sets the [RecyclerView].
     *
     * Override this method to return your customized fragment's [RecyclerView],
     * this fragment will automatically use your specified view to display the items.
     * @return the recycler view for the fragment
     */
    open fun initRecyclerView(): RecyclerView {
        return recycler_data_list
    }

    /**
     * Sets the [AppBarLayout]
     *
     * Override this method to return your customized fragment's [AppBarLayout],
     * this fragment will automatically calculate the height and determine
     * if swipe-to-refresh should be enabled.
     * @return the app bar layout for the fragment
     */
    open fun initAppBarLayout(): AppBarLayout? {
        return null
    }

    /**
     * Sets the [SwipeRefreshLayout]
     *
     * Override this method to return your customized fragment's [SwipeRefreshLayout],
     * this fragment will automatically determine if swipe-to-refresh should be enabled.
     * @return the swipe refresh layout for the fragment
     */
    open fun initSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipe_refresh_data_list
    }

    /** Sets adapter for the [RecyclerView]  */
    abstract fun initRecyclerAdapter(): DataListRecyclerViewAdapter<Any, RecyclerView.ViewHolder>

    /** Sets the layout manager for the [RecyclerView]  */
    abstract fun initRecyclerLayoutManager(): RecyclerView.LayoutManager

    /** Fetch the data from any source  */
    abstract fun fetchData()

    companion object {

        /** Constant key for the parcelable scroll state */
        private const val RECYCLER_SCROLL_STATE = "recycler_scroll_state"
    }
}
