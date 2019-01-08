package com.andy671.shopifycollections.data

data class Product(var name:String,
                   var image:String,
                   var totalAvailableInventory: Int,
                   var customCollection: CustomCollection)

data class CustomCollection(var title:String,
                            var image:String,
                            var bodyHtml: String,
                            var products: List<Product>)