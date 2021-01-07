package com.sehatq.test.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import com.sehatq.test.api.APIService
import com.sehatq.test.model.core.Resource
import com.sehatq.test.model.remote.CategoryProduct
import com.sehatq.test.model.remote.ProductPromo
import com.sehatq.test.util.Constant

class PurchaseHistoryListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resourceAny = MutableLiveData<Resource<Any>>()

    /** The LiveData for list of sample products */
    val dataListCategory = MutableLiveData<List<CategoryProduct>>()

    /** The LiveData for list of sample products */
    val dataListProduct = MutableLiveData<List<ProductPromo>>()

    private var apiService = APIService.apiInterface

    /** The pagination variable */
    private var page = 1
    private val pageLimit = 20

    /** Determines if this view model has successfully fetch a data from local storage */
    private var hasFetchedFromLocal = false

    /** Determines if this view model has completed **ALL** of its fetching process */
    var isLoadFinished = false
        private set

    init {
        dataListCategory.value = ArrayList()
        dataListProduct.value = ArrayList()
    }

    fun fetchPurchasedHistoryData() {
        fetchFromLocal()
    }

    fun onRefresh() {
        page = 1
        hasFetchedFromLocal = false
        isLoadFinished = false
        dataListCategory.value = ArrayList()
        resourceAny.value = Resource.success()
        fetchPurchasedHistoryData()
    }

    private fun fetchFromLocal() {
        if (Hawk.contains(Constant.IS_LIST_PURCHASED_PRODUCT)) {
            val data : ArrayList<ProductPromo> = Hawk.get(Constant.IS_LIST_PURCHASED_PRODUCT)
            dataListProduct.postValue(data)
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
//        apiCaller?.cancel()
    }
}

