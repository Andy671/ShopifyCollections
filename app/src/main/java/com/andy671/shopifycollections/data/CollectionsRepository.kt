package com.andy671.shopifycollections.data

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONObject
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
    fun getCollection(@Query("collection_id") collectionId: Long,
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
    private var collectionList = arrayListOf<CustomCollection>()

    private var collectionListLiveData = MutableLiveData<List<CustomCollection>>()

    companion object {
        private const val BASE_URL = "https://shopicruit.myshopify.com/"
        private const val ACCESS_TOKEN = "c32313df0d0ef512ca64d5b336a0d7c6"
        private const val DEFAULT_PAGE = 1
    }

    private var testUrl = "https://cdn.shopify.com/s/files/1/1000/7970/products/Aerodynamic_20Concrete_20Clock.png?v=1443055734"

    init {
        collectionListLiveData.value = collectionList

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()

        val service = retrofit.create(CollectionsRestApiService::class.java)
        retrieveCustomCollections(service)

    }

    private fun retrieveCustomCollections(service: CollectionsRestApiService) {
        service.getCustomCollections(DEFAULT_PAGE, ACCESS_TOKEN).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body() ?: return
                val jsonObject = JSONObject(responseBody.string())
                val customCollectionsJson = jsonObject.getJSONArray("custom_collections")

                for (i in 0 until customCollectionsJson.length()) {
                    val collectionObj = customCollectionsJson.getJSONObject(i)

                    val id = collectionObj.getLong("id")
                    val title = collectionObj.getString("title")

                    // if the body html and/or image is not specified ignore it.
                    val bodyHtml = collectionObj?.getString("body_html") ?: ""
                    val imageUrl = collectionObj?.getJSONObject("image")?.getString("src") ?: ""

                    collectionList.add(CustomCollection(id, title, bodyHtml, imageUrl))
                }
                collectionListLiveData.value = collectionList

                for (collection in collectionList) {
                    retrieveCollection(service, collection)
                }
            }
        })
    }

    private fun retrieveCollection(service: CollectionsRestApiService, collection: CustomCollection) {
        service.getCollection(collection.id, DEFAULT_PAGE, ACCESS_TOKEN).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body() ?: return
                val jsonObject = JSONObject(responseBody.string())
                val customCollectsJson = jsonObject.getJSONArray("collects")
                val productIds = arrayListOf<Long>()
                for (i in 0 until customCollectsJson.length()) {
                    val productId = customCollectsJson.getJSONObject(i).getLong("product_id")
                    productIds.add(productId)
                }
                retrieveProducts(service, collection, productIds)
            }

        })
    }

    private fun retrieveProducts(service: CollectionsRestApiService, collection: CustomCollection, productIds: List<Long>) {
        service.getProducts(convertLongIdsToString(productIds),
                DEFAULT_PAGE, ACCESS_TOKEN).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseBody = response.body() ?: return
                val jsonObject = JSONObject(responseBody.string())
                val customCollectsJson = jsonObject.getJSONArray("products")
                for (i in 0 until customCollectsJson.length()) {
                    val productObj = customCollectsJson.getJSONObject(i)
                    val productId = productObj.getLong("id")
                    val productTitle = productObj?.getString("title") ?: ""
                    val productImage = productObj?.getJSONObject("image")?.getString("src") ?: ""
                    var totalAvailable = 0
                    for (j in 0 until productObj.getJSONArray("variants").length()) {
                        totalAvailable += productObj.getJSONArray("variants").getJSONObject(j).getInt("inventory_quantity")
                    }
                    collection.products.add(Product(productId, productTitle, productImage, totalAvailable))
                }
                collectionListLiveData.value = collectionList
            }

        })
    }

    private fun convertLongIdsToString(ids: List<Long>): String {
        var string = ""
        ids.forEach { string += it.toString() + "," }
        string = string.substring(0 until string.length - 1)
        Log.d("shopify", string)
        return string
    }


    fun getCollections(): LiveData<List<CustomCollection>> {
        return collectionListLiveData
    }
}