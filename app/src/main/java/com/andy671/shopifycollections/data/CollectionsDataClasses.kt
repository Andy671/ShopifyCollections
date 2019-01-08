package com.andy671.shopifycollections.data

data class Product(var id: Long,
                   var title: String,
                   var imageUrl: String,
                   var totalAvailableInventory: Int)

data class CustomCollection(var id: Long,
                            var title: String,
                            var bodyHtml: String,
                            var imageUrl: String,
                            var products: ArrayList<Product> = arrayListOf())