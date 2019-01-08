package com.andy671.shopifycollections.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.andy671.shopifycollections.data.CustomCollection
import com.andy671.shopifycollections.data.Product
import kotlin.random.Random

class CollectionsViewModel : ViewModel(){

    private var collectionList = arrayListOf<CustomCollection>()
    private var productList = arrayListOf<Product>()

    private var collectionListLiveData = MutableLiveData<List<CustomCollection>>()
    private var productListLiveData = MutableLiveData<List<Product>>()

    init{
        collectionListLiveData.value = collectionList
        productListLiveData.value = productList

        collectionList.add(CustomCollection("Test 1", "Image", "Body", arrayListOf()))
        collectionList.add(CustomCollection("Test 2", "Image", "Body", arrayListOf()))
        collectionList.add(CustomCollection("Test 3", "Image", "Body", arrayListOf()))
        collectionList.add(CustomCollection("Test 4", "Image", "Body", arrayListOf()))
        collectionList.add(CustomCollection("Test 5", "Image", "Body", arrayListOf()))

    }

    fun getCollections(): LiveData<List<CustomCollection>>{
        return collectionListLiveData
    }

    fun getCurrentProducts(): LiveData<List<Product>>{
        return productListLiveData
    }

    fun addDumbData(){
        collectionList.add(CustomCollection("Test ${Random.nextInt(100)}", "Image", "Body", arrayListOf()))
        collectionListLiveData.value = collectionList
    }

    fun removeDumbData(){
        if (collectionList.size > 0) {
            collectionList.removeAt(collectionList.size - 1)
            collectionListLiveData.value = collectionList
        }
    }

}