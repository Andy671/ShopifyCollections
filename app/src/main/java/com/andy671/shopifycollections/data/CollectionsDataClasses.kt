package com.andy671.shopifycollections.data

import kotlinx.serialization.Serializable

@Serializable
data class JsonCustomCollectionList(val custom_collections: List<JsonCustomCollection>)

@Serializable
data class JsonCustomCollection(val id: Long,
                                val title: String,
                                val body_html: String,
                                val image: JsonImage)

@Serializable
data class JsonImage(val src: String)

@Serializable
data class JsonCollects(val collects: List<JsonCollect>)

@Serializable
data class JsonCollect(val product_id: Long)


@Serializable
data class JsonProducts(val products: List<JsonProduct>)

@Serializable
data class JsonProduct(val id: Long,
                       val title: String,
                       val image: JsonImage,
                       val variants: List<JsonProductVariant>)

@Serializable
data class JsonProductVariant(val inventory_quantity: Int)


data class Product(var id: Long,
                   var title: String,
                   var imageUrl: String,
                   var totalAvailableInventory: Int)

data class CustomCollection(var id: Long,
                            var title: String,
                            var bodyHtml: String,
                            var imageUrl: String,
                            var products: ArrayList<Product> = arrayListOf())