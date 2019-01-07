package com.andy671.shopifycollections

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection.*
import kotlinx.android.synthetic.main.item_collection_details.*
import kotlinx.android.synthetic.main.item_product.*

class CollectionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        Glide.with(this)
                .load(R.raw.testcollection)
                .into(image_collection_details)

        Glide.with(this)
                .load(R.raw.testcollection)
                .into(image_collection)

        Glide.with(this)
                .load(R.raw.testproduct)
                .into(image_product)

    }
}
