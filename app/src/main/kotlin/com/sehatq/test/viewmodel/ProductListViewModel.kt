package com.sehatq.test.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import com.sehatq.test.api.APIService
import com.sehatq.test.api.util.CustomObserver
import com.sehatq.test.model.core.CustomResponse
import com.sehatq.test.model.core.Resource
import com.sehatq.test.model.remote.CategoryProduct
import com.sehatq.test.model.remote.ProductData
import com.sehatq.test.model.remote.ProductPromo
import com.sehatq.test.util.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductListViewModel : ViewModel(), LifecycleObserver {

    /** The LiveData for resource state */
    val resource = MutableLiveData<Resource<Any>>()

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

    fun fetchData(isFetchedFromLocal: Boolean) {
        if (isFetchedFromLocal) fetchFromLocal() else fetchFromRemote()
    }

    fun onRefresh() {
        page = 1
        hasFetchedFromLocal = false
        isLoadFinished = false
        dataListCategory.value = ArrayList()
        resource.value = Resource.success()
        fetchData(true)
    }

    private fun fetchFromRemote() {
        resource.postValue(Resource.loading())
        apiService.getProductList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CustomObserver<Any>() {
                override fun onSuccess(response: Any) {
                    val dataResponse = response as ArrayList<CustomResponse<ProductData>>
                    val data = dataResponse[0].data
                    val productData = data as ProductData

                    isLoadFinished = true
                    resource.postValue(Resource.success(data))
                    dataListCategory.postValue(productData.categoryProductList)
                    dataListProduct.postValue(productData.productPromoList)

                    Hawk.put((Constant.IS_LIST_PRODUCT), productData)
                }

                override fun onFailure(error: Any) {
//                    resource.postValue(Resource.error())
                }
            })
    }

    private fun fetchFromLocal() {
        if (Hawk.contains(Constant.IS_LIST_PRODUCT)) {
            val productData: ProductData = Hawk.get(Constant.IS_LIST_PRODUCT)
            dataListProduct.postValue(productData.productPromoList)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun cancel() {
//        apiCaller?.cancel()
    }
}

