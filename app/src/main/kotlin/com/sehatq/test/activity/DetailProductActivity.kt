package com.sehatq.test.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.orhanobut.hawk.Hawk
import com.sehatq.test.R
import com.sehatq.test.R.string
import com.sehatq.test.activity.core.CoreActivity
import com.sehatq.test.model.remote.ProductPromo
import com.sehatq.test.util.Common
import com.sehatq.test.util.Constant
import kotlinx.android.synthetic.main.activity_detail_product.btn_buy
import kotlinx.android.synthetic.main.activity_detail_product.btn_share
import kotlinx.android.synthetic.main.activity_detail_product.img_detail_product
import kotlinx.android.synthetic.main.activity_detail_product.img_favorite
import kotlinx.android.synthetic.main.activity_detail_product.txt_description
import kotlinx.android.synthetic.main.activity_detail_product.txt_name_product
import kotlinx.android.synthetic.main.activity_detail_product.txt_price
import kotlinx.android.synthetic.main.include_toolbar_search.ic_back
import kotlinx.android.synthetic.main.include_toolbar_search.layout_toolbar_detail

class DetailProductActivity : CoreActivity(), View.OnClickListener {

  private var dataProduct : ProductPromo? = null
  override val viewRes = R.layout.activity_detail_product

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    initDetailProduct()
  }

  override fun onClick(v: View?) {
    when (v) {
      ic_back -> {
        onBackPressed()
      } btn_buy -> {
        initAddDataProduct()
      } btn_share -> {
        shareData(dataProduct?.title)
      } img_favorite -> {
        Common.showToast(getString(string.info_success_favorite_product))
      }
    }
  }

  private fun initDetailProduct() {
    layout_toolbar_detail.visibility  = View.VISIBLE

    if (Hawk.contains(Constant.IS_DATA_PRODUCT)) {
      dataProduct = Hawk.get(Constant.IS_DATA_PRODUCT)

      if (dataProduct?.loved == 0)
        img_favorite.setImageDrawable(getDrawable(R.drawable.ic_heart_disable))
      else
        img_favorite.setImageDrawable(getDrawable(R.drawable.ic_heart))

      img_detail_product.setImageURI(dataProduct?.imageUrl)
      txt_name_product.text = dataProduct?.title
      txt_description.text = dataProduct?.description
      txt_price.text = dataProduct?.price
    }

    ic_back.setOnClickListener(this)
    btn_buy.setOnClickListener(this)
    btn_share.setOnClickListener(this)
    img_favorite.setOnClickListener(this)
  }

  private fun initAddDataProduct() {
    var productPurchasedList: ArrayList<ProductPromo> = ArrayList()
    if (Hawk.contains(Constant.IS_LIST_PURCHASED_PRODUCT)) {
      productPurchasedList = Hawk.get(Constant.IS_LIST_PURCHASED_PRODUCT)
      dataProduct?.let {
        productPurchasedList.add(it)
      }

      Hawk.delete(Constant.IS_LIST_PURCHASED_PRODUCT)
      Hawk.put((Constant.IS_LIST_PURCHASED_PRODUCT), productPurchasedList)
    } else {
      if (dataProduct != null) productPurchasedList.add(dataProduct!!)
      Hawk.put((Constant.IS_LIST_PURCHASED_PRODUCT), productPurchasedList)
    }

    PurchaseProductHistoryActivity.launchIntent(this)
  }

  fun shareData(text: String?){
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, text)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    startActivity(Intent.createChooser(intent, "Share using"))
  }

  companion object {
    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context, productPromo: ProductPromo) {
      val intent = Intent(context, DetailProductActivity::class.java)
      Hawk.delete(Constant.IS_LIST_PRODUCT)
      Hawk.put((Constant.IS_DATA_PRODUCT), productPromo)

      context.startActivity(intent)
    }
  }
}