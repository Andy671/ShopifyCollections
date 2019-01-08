package com.andy671.shopifycollections.data

data class Product(var name: String,
                   var imageUrl: String,
                   var totalAvailableInventory: Int)

data class CustomCollection(var title: String,
                            var imageUrl: String,
                            var bodyHtml: String,
                            var products: ArrayList<Product>)