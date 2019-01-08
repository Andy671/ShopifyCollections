package com.andy671.shopifycollections.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.andy671.shopifycollections.R

class CollectionDetailsFragment : Fragment() {

    private lateinit var mViewModel: CollectionsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_collection_details, container, false)

        activity?.run {
            mViewModel = ViewModelProviders.of(this).get(CollectionsViewModel::class.java)
        } ?: throw Exception("Wrong Activity")

        fragmentView.findViewById<Button>(R.id.button_add).setOnClickListener{
            mViewModel.addDumbData()
        }

        fragmentView.findViewById<Button>(R.id.button_remove).setOnClickListener{
            mViewModel.removeDumbData()
        }

        return fragmentView
    }

}
