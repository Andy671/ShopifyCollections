package com.andy671.shopifycollections.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.andy671.shopifycollections.data.CustomCollection
import com.andy671.shopifycollections.data.Product
import kotlin.random.Random

class CollectionsViewModel : ViewModel() {
    enum class Page {
        CollectionList,
        CollectionDetails
    }

    private var collectionList = arrayListOf<CustomCollection>()
    private var productList = arrayListOf<Product>()

    private var pageLiveData = MutableLiveData<Page>()
    private var collectionListLiveData = MutableLiveData<List<CustomCollection>>()
    private var productListLiveData = MutableLiveData<List<Product>>()

    private var testUrl = "https://cdn.shopify.com/s/files/1/1000/7970/products/Aerodynamic_20Concrete_20Clock.png?v=1443055734"

    init {
        pageLiveData.value = Page.CollectionList
        collectionListLiveData.value = collectionList
        productListLiveData.value = productList

        collectionList.add(CustomCollection("Collection 1", testUrl, "Body", getDumbProducts()))
        collectionList.add(CustomCollection("Collection 2", testUrl, "Body", getDumbProducts()))
        collectionList.add(CustomCollection("Collection 3", testUrl, "Body", getDumbProducts()))
        collectionList.add(CustomCollection("Collection 4", testUrl, "Body", getDumbProducts()))
        collectionList.add(CustomCollection("Collection 5", testUrl, "Body", getDumbProducts()))
    }

    private fun getDumbProducts() : ArrayList<Product>{
        val list = arrayListOf<Product>()
        for (i in 0..Random.nextInt(40)) {
            list.add(Product("Product ${Random.nextInt(100)}", testUrl, Random.nextInt(200)))
        }
        return list
    }

    fun getPage(): LiveData<Page>{
        return pageLiveData
    }

    fun getCollections(): LiveData<List<CustomCollection>> {
        return collectionListLiveData
    }

    fun getCurrentProducts(): LiveData<List<Product>> {
        return productListLiveData
    }

    fun onClickCollectionCard(collection: CustomCollection) {
        productList = collection.products
        productListLiveData.value = productList
        pageLiveData.value = Page.CollectionDetails
    }

    fun onBackPressed(){
        pageLiveData.value = Page.CollectionList
    }

}