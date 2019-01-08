package com.andy671.shopifycollections.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.andy671.shopifycollections.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection.*
import kotlinx.android.synthetic.main.item_collection_info.*
import kotlinx.android.synthetic.main.item_product.*

class CollectionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)
    }
}
