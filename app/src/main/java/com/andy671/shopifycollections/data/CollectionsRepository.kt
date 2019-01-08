package com.andy671.shopifycollections.data

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import kotlinx.serialization.json.JSON
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface CollectionsRestApiService {
    //    https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6
    @GET("/admin/custom_collections.json")
    fun getCustomCollections(@Query("page") page: Int = 1,
                             @Query("access_token") accessToken: String)
            : Call<ResponseBody>

    @GET("/admin/collects.json")
    fun getCollects(@Query("collection_id") collectionId: Long,
                    @Query("page") page: Int,
                    @Query("access_token") accessToken: String)
            : Call<ResponseBody>

    @GET("/admin/products.json")
    fun getProducts(@Query("ids") productIds: String,
                    @Query("page") page: Int,
                    @Query("access_token") accessToken: String)
            : Call<ResponseBody>

}

class CollectionsRepository(val application: Application) {

    companion object {
        private const val BASE_URL = "https://shopicruit.myshopify.com/"
        private const val ACCESS_TOKEN = "c32313df0d0ef512ca64d5b336a0d7c6"
        private const val DEFAULT_PAGE = 1
    }

    private var collectionList = arrayListOf<CustomCollection>()
    private var collectionListLiveData = MutableLiveData<List<CustomCollection>>()
    private lateinit var service: CollectionsRestApiService

    init {
        collectionListLiveData.value = collectionList

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()

        service = retrofit.create(CollectionsRestApiService::class.java)
    }

    fun retrieveCustomCollections() {
        service.getCustomCollections(DEFAULT_PAGE, ACCESS_TOKEN).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body() ?: return
                val list = JSON.nonstrict.parse(JsonCustomCollections.serializer(), responseBody.string())
                for (collection in list.custom_collections) {
                    collectionList.add(CustomCollection(collection.id, collection.title, collection.body_html, collection.image.src))
                }
                collectionListLiveData.value = collectionList
            }
        })
    }

    fun retrieveCollectionProducts(position: Int) {
        if (collectionList[position].products.isEmpty())
            retrieveCollects(collectionList[position])
    }

    fun getCollections(): LiveData<List<CustomCollection>> {
        return collectionListLiveData
    }

    private fun retrieveCollects(collection: CustomCollection) {
        service.getCollects(collection.id, DEFAULT_PAGE, ACCESS_TOKEN).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body() ?: return
                val list = JSON.nonstrict.parse(JsonCollects.serializer(), responseBody.string())
                val productIdsList = arrayListOf<Long>()
                for (collect in list.collects) {
                    productIdsList.add(collect.product_id)
                }
                val productIds = productIdsList.joinToString(separator = ",")
                retrieveProducts(service, collection, productIds)
            }

        })
    }

    private fun retrieveProducts(service: CollectionsRestApiService, collection: CustomCollection, productIds: String) {
        service.getProducts(productIds, DEFAULT_PAGE, ACCESS_TOKEN).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body() ?: return
                val list = JSON.nonstrict.parse(JsonProducts.serializer(), responseBody.string())
                for (product in list.products) {
                    val totalAvailableInventory = product.variants.sumBy { it.inventory_quantity }
                    collection.products.add(
                            Product(product.id, product.title, product.image.src, totalAvailableInventory))
                }
                collectionListLiveData.value = collectionList
            }

        })
    }
}