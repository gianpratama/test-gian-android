package com.sehatq.test.adapter.recycler.core

import android.os.AsyncTask
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.test.App
import com.sehatq.test.R
import com.sehatq.test.model.core.Resource
import com.sehatq.extension.android.view.inflate
import kotlinx.android.synthetic.main.base_adapter_recycler_empty.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_error_full.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_error_pagination.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_loading_full.view.*
import kotlinx.android.synthetic.main.base_adapter_recycler_loading_pagination.view.*

/**
 * A base adapter class for data list recycler views.
 *
 * This class can display a customized empty layout, error layout, and progress layout.
 * This class can also display an error layout and progress layout for pagination loads.
 *
 * To use a custom layout for each view type, simply override each create and bind methods.
 */
abstract class DataListRecyclerViewAdapter<DATA, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /** The data set for this adapter */
    private var actualDataList: MutableList<DATA> = ArrayList()

    /**
     * The filtered data set for this adapter.
     * This is the data that is actually shown.
     */
    private var filteredDataList: MutableList<DATA> = ArrayList()

    /**
     * The content state.
     * This state determines if an additional item count is required to show the additional view holder.
     */
    private var state: Int = NORMAL

    /** Determines if an additional row should be shown, default value is true */
    private var hasExtraRow = true
        set(value) {
            val isChanged = (field != value)
            val hadExtraRow = field
            field = value
            if (isChanged) {
                if (hadExtraRow) notifyItemRemoved(itemCount)
                else notifyItemInserted(itemCount)
            } else {
                notifyItemChanged(itemCount - 1)
            }
            if (recyclerView != null && recyclerView!!.layoutManager != null) handleExtraRowLayoutManager(recyclerView!!.layoutManager!!)
        }

    /**
     * Obtain the state.
     * This state will only take effect on certain items.
     * @return the state
     */
    fun getState() : Int {
        return state
    }

    /**
     * Sets the state and notifies the changes.
     * This state will only take effect on certain items.
     * @param state the state
     */
    fun setState(state: Int) {
        val previousState = this.state
        this.state = state
        if (hasExtraRow && previousState != state) notifyItemChanged(itemCount - 1)
        if (recyclerView != null && recyclerView!!.layoutManager != null) handleExtraRowLayoutManager(recyclerView!!.layoutManager!!)
    }

    /**
     * Sets the state from the available resource.
     * @param resource the resource
     */
    fun setState(resource: Resource<*>?) {
        when (resource?.status) {
            Resource.LOADING -> {
                setState(LOADING)
            }
            Resource.ERROR -> {
                errorText = resource.error.message
                setState(ERROR)
            }
            Resource.SUCCESS -> {
                setState(NORMAL)
            }
        }
    }

    /** Handle custom layout manager. Override this method if required */
    fun handleExtraRowLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val size = getDataList().size
                    return if (position >= size) layoutManager.spanCount
                    else 1
                }
            }
        }
    }

    /**
     * The empty text for the default [EmptyViewHolder].
     * This text will only be displayed on [IMPL_EMPTY].
     */
    var emptyText: String? = null

    /**
     * The loading text for the default [LoadingFullViewHolder] and [LoadingPaginationViewHolder].
     * This text will only be displayed on [IMPL_LOADING_FULL] and [IMPL_LOADING_PAGINATION].
     */
    var loadingText: String? = null

    /**
     * The empty text for the default [ErrorFullViewHolder] and [ErrorPaginationViewHolder].
     * This text will only be displayed on [IMPL_ERROR_FULL] and [IMPL_ERROR_PAGINATION].
     */
    var errorText: String? = null

    /** The listener for retry button */
    var retryListener: OnRetryListener? = null
        private set

    /** The interface for data list filter */
    private var filterer: DataListFilter<DATA>? = null

    /** The background task for filtering */
    private var filterTask: FilterTask<DATA>? = null

    /** The [DiffUtilNotifier] interface */
    private var diffUtilNotifier: DiffUtilNotifier<DATA>? = null

    /** The recycler view attached to this adapter */
    private var recyclerView: RecyclerView? = null

    /** Flag to determine if this adapter is currently trying to restore its [RecyclerView] state */
    var isRestoringRecyclerState = false

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow && position == itemCount - 1) {
            if (state == LOADING) if (getDataList().isEmpty()) IMPL_LOADING_FULL else IMPL_LOADING_PAGINATION
            else if (state == ERROR) if (getDataList().isEmpty()) IMPL_ERROR_FULL else IMPL_ERROR_PAGINATION
            else if (getDataList().isEmpty()) IMPL_EMPTY else IMPL_NULL
        } else {
            getDataItemViewType(position)
        }
    }

    open fun getDataItemViewType(position: Int): Int {
        return IMPL_DATA
    }

    override fun getItemCount(): Int {
        return filteredDataList.size + if (hasExtraRow) 1 else 0
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IMPL_NULL -> onCreateNullViewHolder(parent)
            IMPL_EMPTY -> onCreateEmptyViewHolder(parent)
            IMPL_LOADING_FULL -> onCreateLoadingFullViewHolder(parent)
            IMPL_LOADING_PAGINATION -> onCreateLoadingPaginationViewHolder(parent)
            IMPL_ERROR_FULL -> onCreateErrorFullViewHolder(parent)
            IMPL_ERROR_PAGINATION -> onCreateErrorPaginationViewHolder(parent)
            else -> onCreateDataViewHolder(parent, viewType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            IMPL_NULL -> onBindNullViewHolder(holder)
            IMPL_EMPTY -> onBindEmptyViewHolder(holder)
            IMPL_LOADING_FULL -> onBindLoadingFullViewHolder(holder)
            IMPL_LOADING_PAGINATION -> onBindLoadingPaginationViewHolder(holder)
            IMPL_ERROR_FULL -> onBindErrorFullViewHolder(holder)
            IMPL_ERROR_PAGINATION -> onBindErrorPaginationViewHolder(holder)
            else -> onBindDataViewHolder(holder as VH, viewType)
        }
    }

    /**
     * Obtain the complete data list used by this adapter.
     * @return the data as list
     */
    fun getActualDataList() : MutableList<DATA> {
        return actualDataList
    }

    /**
     * Obtain the filtered data list used by this adapter.
     * @return the data as list
     */
    fun getFilteredDataList() : MutableList<DATA> {
        return filteredDataList
    }

    /**
     * Obtain the shown data list used by this adapter.
     * @return the data as list
     */
    fun getDataList() : MutableList<DATA> {
        return filteredDataList
    }

    /**
     * Sets the data set for this adapter.
     * @param list the new list
     * @param listener the listener for data filtering
     */
    fun setDataList(list: Collection<DATA>?, listener: OnDataListFilterListener? = null) {
        actualDataList.clear()
        if (list != null) actualDataList.addAll(list)
        filterDataList(listener)
    }

    /**
     * Sets the data set for this adapter.
     * @param list the new list
     * @param listener the listener for data filtering
     */
    fun setDataList(list: Collection<DATA>?, listener: OnDataListFilterListenerImpl) {
        setDataList(list, object : OnDataListFilterListener {
            override fun onDataListFilteringCompleted() {
                listener.invoke()
            }
        })
    }

    /**
     * Sets the retry button listener.
     * @param onRetryClicked called function when retry in error layout is clicked
     */
    fun setOnRetryListener(onRetryClicked: OnRetryClickedImpl) {
        setOnRetryListener(object : OnRetryListener {
            override fun onRetryClicked() {
                onRetryClicked.invoke()
            }
        })
    }

    /**
     * Sets the retry button listener.
     * @param listener the listener
     */
    fun setOnRetryListener(listener: OnRetryListener?) {
        retryListener = listener
    }

    /** @return the data list filter interface */
    fun getDataListFilter(): DataListFilter<DATA>? {
        return filterer
    }

    /**
     * Sets the data list filter interface.
     * @param filter the interface
     */
    fun setDataListFilter(filter: PerformDataListFilteringImpl<DATA>) {
        setDataListFilter(object : DataListFilter<DATA> {
            override fun performDataListFiltering(data: DATA): Boolean {
                return filter(data)
            }
        })
    }

    /**
     * Sets the data list filter interface.
     * @param filter the interface
     */
    fun setDataListFilter(filter: DataListFilter<DATA>?) {
        filterer = filter
        filterDataList()
    }

    /** Filters the data list for this adapter  */
    private fun filterDataList(listener: OnDataListFilterListener? = null) {
        filterTask?.cancel(true)
        val oldList = ArrayList(filteredDataList)

        if (filterer == null) {
            filteredDataList.clear()
            filteredDataList.addAll(actualDataList)
            if (recyclerView != null && oldList.isEmpty()) {
                if (!isRestoringRecyclerState) notifyItemRangeChanged(0, filteredDataList.size)
            } else {
                notifyDataSetDiffUtil(oldList, filteredDataList)
            }
            listener?.onDataListFilteringCompleted()
        } else {
            filterTask = FilterTask(actualDataList, filterer!!, object : FilterTaskListener<DATA> {
                override fun onFilterTaskListenerCompleted(filter: DataListFilter<DATA>, dataList: List<DATA>) {
                    filteredDataList.clear()
                    filteredDataList.addAll(dataList)
                    if (recyclerView != null && oldList.isEmpty()) {
                        if (!isRestoringRecyclerState) notifyItemRangeChanged(0, filteredDataList.size)
                    } else {
                        notifyDataSetDiffUtil(oldList, filteredDataList)
                    }
                    listener?.onDataListFilteringCompleted()
                }
            })
            filterTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    /** @return the diff util callback notifier for this adapter */
    fun getDiffUtilNotifier(): DiffUtilNotifier<DATA>? {
        return diffUtilNotifier
    }

    /** Sets the diff util callback notifier for this adapter */
    fun setDiffUtilNotifier(notifier: GenerateDiffUtilCallbackImpl<DATA>) {
        setDiffUtilNotifier(object : DiffUtilNotifier<DATA> {
            override fun generateDiffUtilCallback(oldList: List<DATA>, newList: List<DATA>): DiffUtil.Callback {
                return notifier(oldList, newList)
            }
        })
    }

    /** Sets the diff util callback notifier for this adapter */
    fun setDiffUtilNotifier(notifier: DiffUtilNotifier<DATA>?) {
        diffUtilNotifier = notifier
    }

    /** Notify this adapter on data changes */
    private fun notifyDataSetDiffUtil(oldList: List<DATA>, newList: List<DATA>) {
        if (diffUtilNotifier != null) {
            val result = DiffUtil.calculateDiff(diffUtilNotifier!!.generateDiffUtilCallback(oldList, newList))
            result.dispatchUpdatesTo(this)
        } else {
            notifyDataSetChanged()
        }
    }

    /** Creates the [NullViewHolder] */
    open fun onCreateNullViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NullViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_null))
    }

    /** Creates the [EmptyViewHolder] */
    open fun onCreateEmptyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return EmptyViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_empty))
    }

    /** Creates the [LoadingFullViewHolder] */
    open fun onCreateLoadingFullViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingFullViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_loading_full))
    }

    /** Creates the [LoadingPaginationViewHolder] */
    open fun onCreateLoadingPaginationViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingPaginationViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_loading_pagination))
    }

    /** Creates the [ErrorFullViewHolder] */
    open fun onCreateErrorFullViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ErrorFullViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_error_full))
    }

    /** Creates the [ErrorPaginationViewHolder] */
    open fun onCreateErrorPaginationViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ErrorPaginationViewHolder<Any?, Any?>(parent.inflate(R.layout.base_adapter_recycler_error_pagination))
    }

    /** Binds the [NullViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindNullViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as NullViewHolder<*, *>).bindView()
    }

    /** Binds the [EmptyViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindEmptyViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as EmptyViewHolder<*, *>).bindView()
    }

    /** Binds the [LoadingFullViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindLoadingFullViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as LoadingFullViewHolder<*, *>).bindView()
    }

    /** Binds the [LoadingPaginationViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindLoadingPaginationViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as LoadingPaginationViewHolder<*, *>).bindView()
    }

    /** Binds the [ErrorFullViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindErrorFullViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as ErrorFullViewHolder<*, *>).bindView()
    }

    /** Binds the [ErrorPaginationViewHolder] */
    @Suppress("UNCHECKED_CAST")
    open fun onBindErrorPaginationViewHolder(holder: RecyclerView.ViewHolder) {
        (holder as ErrorPaginationViewHolder<*, *>).bindView()
    }

    /** The empty [RecyclerView.ViewHolder] */
    inner class NullViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView() {
            // Do nothing
        }
    }

    /** The empty [RecyclerView.ViewHolder] */
    inner class EmptyViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView() {
            if (emptyText.isNullOrEmpty()) emptyText = App.context.resources.getString(R.string.info_no_data)
            itemView.txt_empty.text = emptyText
        }
    }

    /** The progress [RecyclerView.ViewHolder] */
    inner class LoadingFullViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView() {
            itemView.txt_loading.setText(loadingText)
            if (loadingText.isNullOrEmpty()) itemView.txt_loading.visibility = View.GONE
            else itemView.txt_loading.visibility = View.VISIBLE
        }
    }

    /** The progress [RecyclerView.ViewHolder] */
    inner class LoadingPaginationViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView() {
            itemView.txt_loading_pagination.setText(loadingText)
            if (loadingText.isNullOrEmpty()) itemView.txt_loading_pagination.visibility = View.GONE
            else itemView.txt_loading_pagination.visibility = View.VISIBLE
        }
    }

    /** The error [RecyclerView.ViewHolder] */
    inner class ErrorFullViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.btn_retry.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (view == itemView.btn_retry) retryListener?.onRetryClicked()
        }

        fun bindView() {
            itemView.txt_error.text = errorText
            if (errorText.isNullOrEmpty()) itemView.txt_error.visibility = View.GONE
            else itemView.txt_error.visibility = View.VISIBLE
        }
    }

    /** The error [RecyclerView.ViewHolder] */
    inner class ErrorPaginationViewHolder<T, U>(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (view == itemView) retryListener?.onRetryClicked()
        }

        fun bindView() {
            itemView.txt_error_pagination.text = errorText
            if (errorText.isNullOrEmpty()) itemView.txt_error_pagination.visibility = View.GONE
            else itemView.txt_error_pagination.visibility = View.VISIBLE
        }
    }

    /** The static task to do filtering in background */
    private class FilterTask<DATA>(
            val initialData: List<DATA>,
            val filterer: DataListFilter<DATA>,
            val taskListener: FilterTaskListener<DATA>)
        : AsyncTask<String, String, List<DATA>>() {

        private val dataList = ArrayList<DATA>()

        override fun doInBackground(vararg strings: String): List<DATA> {
            for (i in initialData.indices) {
                val data = initialData[i]
                if (filterer.performDataListFiltering(data)) dataList.add(data)
            }
            return dataList
        }

        override fun onPostExecute(data: List<DATA>) {
            super.onPostExecute(data)
            taskListener.onFilterTaskListenerCompleted(filterer, data)
        }
    }

    /** Create the [RecyclerView.ViewHolder] for normal layout */
    abstract fun onCreateDataViewHolder(parent: ViewGroup, viewType: Int): VH

    /** Binds the [RecyclerView.ViewHolder] for normal layout */
    abstract fun onBindDataViewHolder(holder: VH, position: Int)

    /** The interface for retry functions */
    interface OnRetryListener {

        /** Called when the retry button on the error layout is clicked */
        fun onRetryClicked()
    }

    /** The interface for data filtering on data set update */
    interface OnDataListFilterListener {

        /** Called if filtering has been done */
        fun onDataListFilteringCompleted()
    }

    /** The interface for data filtering */
    interface DataListFilter<DATA> {

        /** @return true if the item is included in the filter */
        fun performDataListFiltering(data: DATA): Boolean
    }

    /** The internal interface for data filtering */
    private interface FilterTaskListener<DATA> {

        /** Called when internal filtering task is completed */
        fun onFilterTaskListenerCompleted(filter: DataListFilter<DATA>, dataList: List<DATA>)
    }

    /** The interface to generate a [DiffUtil.Callback] */
    interface DiffUtilNotifier<DATA> {

        /** @return the required [DiffUtil.Callback] */
        fun generateDiffUtilCallback(oldList: List<DATA>, newList: List<DATA>): DiffUtil.Callback
    }

    companion object {

        const val NORMAL = 1000
        const val LOADING = 1001
        const val ERROR = 1002
        private const val IMPL_DATA = 2000
        private const val IMPL_NULL = 2001
        private const val IMPL_EMPTY = 2002
        private const val IMPL_LOADING_FULL = 2003
        private const val IMPL_LOADING_PAGINATION = 2004
        private const val IMPL_ERROR_FULL = 2005
        private const val IMPL_ERROR_PAGINATION = 2006
    }
}

typealias OnRetryClickedImpl = (() -> Unit)

typealias OnDataListFilterListenerImpl = (() -> Unit)

typealias PerformDataListFilteringImpl<DATA> = ((data: DATA) -> Boolean)

typealias GenerateDiffUtilCallbackImpl<DATA> = ((oldList: List<DATA>, newList: List<DATA>) -> DiffUtil.Callback)
