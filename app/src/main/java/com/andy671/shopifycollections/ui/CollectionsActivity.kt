package com.andy671.shopifycollections.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.andy671.shopifycollections.R

class CollectionsActivity : AppCompatActivity() {

    private lateinit var mViewModel: CollectionsViewModel

    companion object {
        private const val COLLECTION_LIST_FRAGMENT_TAG: String = "COLLECTION_LIST"
        private const val COLLECTION_DETAILS_FRAGMENT_TAG: String = "COLLECTION_DETAILS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        val manager = supportFragmentManager

        if (savedInstanceState == null) {
            val collectionListFragment = CollectionListFragment()
            val collectionDetailsFragment = CollectionDetailsFragment()
            manager.beginTransaction()
                    .add(R.id.frame_fragment_container, collectionListFragment, COLLECTION_LIST_FRAGMENT_TAG)
                    .add(R.id.frame_fragment_container, collectionDetailsFragment, COLLECTION_DETAILS_FRAGMENT_TAG)
                    .hide(collectionDetailsFragment)
                    .commit()
        }

        mViewModel = ViewModelProviders.of(this).get(CollectionsViewModel::class.java)
        mViewModel.getPage().observe(this, Observer {
            val collectionListFragment = manager.findFragmentByTag(COLLECTION_LIST_FRAGMENT_TAG)!!
            val collectionDetailsFragment = manager.findFragmentByTag(COLLECTION_DETAILS_FRAGMENT_TAG)!!

            if (it == CollectionsViewModel.Page.CollectionList) {
                manager.beginTransaction()
                        .show(collectionListFragment)
                        .hide(collectionDetailsFragment)
                        .commit()
            } else if (it == CollectionsViewModel.Page.CollectionDetails) {
                manager.beginTransaction()
                        .show(collectionDetailsFragment)
                        .hide(collectionListFragment)
                        .commit()
            }

        })
    }

    override fun onBackPressed() {
        mViewModel.onBackPressed()
    }
}
