package com.andy671.shopifycollections.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andy671.shopifycollections.R
import com.andy671.shopifycollections.data.Product
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*

class CollectionDetailsFragment : Fragment() {

    private lateinit var mViewModel: CollectionsViewModel
    private lateinit var mListAdapter: CollectionDetailsAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_collection_details, container, false)

        activity?.run {
            mViewModel = ViewModelProviders.of(this).get(CollectionsViewModel::class.java)
        } ?: throw Exception("Wrong Activity")

        mListAdapter = CollectionDetailsAdapter()

        mRecyclerView = fragmentView.findViewById<RecyclerView>(R.id.recycler_collection_details)
        mRecyclerView.adapter = mListAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mViewModel.getCurrentProducts().observe(this, Observer {
            mRecyclerView.scrollToPosition(0)
            mListAdapter.currentList = it as ArrayList<Product>
            mListAdapter.notifyDataSetChanged()
        })

        return fragmentView
    }

    inner class CollectionDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var holderView: View = view

        fun bind(product: Product) {
            holderView.text_product_name.text = product.name
            holderView.text_product_total_inventory.text = product.totalAvailableInventory.toString()
            Glide.with(holderView.context)
                    .load(product.imageUrl)
                    .into(holderView.image_product)
        }
    }

    inner class CollectionDetailsAdapter : RecyclerView.Adapter<CollectionDetailsViewHolder>() {

        var currentList: ArrayList<Product> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionDetailsViewHolder {
            val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_product, parent, false)
            return CollectionDetailsViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CollectionDetailsViewHolder, position: Int) {
            holder.bind(currentList[position])
        }

        override fun getItemCount(): Int {
            return currentList.size
        }

    }
}
