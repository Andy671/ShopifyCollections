package com.andy671.shopifycollections.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.andy671.shopifycollections.R
import com.andy671.shopifycollections.data.CustomCollection
import com.andy671.shopifycollections.data.Product
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection.view.*
import kotlinx.android.synthetic.main.item_collection_info.view.*
import kotlinx.android.synthetic.main.item_product.view.*

class CollectionDetailsFragment : Fragment() {

    companion object {
        private const val VIEW_TYPE_PRODUCT = 0
        private const val VIEW_TYPE_COLLECTION_INFO = 1
    }

    private lateinit var mViewModel: CollectionsViewModel
    private lateinit var mListAdapter: CollectionDetailsAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_collection_details, container, false)

        activity?.run {
            mViewModel = ViewModelProviders.of(this, CollectionsViewModelFactory(application))
                    .get(CollectionsViewModel::class.java)
        } ?: throw Exception("Wrong Activity")

        mListAdapter = CollectionDetailsAdapter()

        mRecyclerView = fragmentView.findViewById(R.id.recycler_collection_details)
        mRecyclerView.adapter = mListAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mViewModel.getCurrentCollection().observe(this, Observer {
            if (it is CustomCollection) {
                mRecyclerView.scrollToPosition(0)
                mListAdapter.collection = it
                mListAdapter.notifyDataSetChanged()
                if (mListAdapter.collection.products.size > 0) {
                    fragmentView.findViewById<ProgressBar>(R.id.progress_bar_details).visibility = View.GONE
                    mRecyclerView.visibility = View.VISIBLE
                } else {
                    fragmentView.findViewById<ProgressBar>(R.id.progress_bar_details).visibility = View.VISIBLE
                    mRecyclerView.visibility = View.GONE
                }
            }
        })

        return fragmentView
    }

    inner class ProductViewHolder(private val holderView: View) : RecyclerView.ViewHolder(holderView) {

        fun bind(product: Product) {
            holderView.text_product_title.text = product.title
            val html = resources.getString(R.string.total_available_inventory, product.totalAvailableInventory)
            holderView.text_product_total_inventory.text = Html.fromHtml(html)
            Glide.with(holderView.context)
                    .load(product.imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holderView.image_product)
        }
    }

    inner class CollectionInfoViewHolder(private val holderView: View) : RecyclerView.ViewHolder(holderView) {

        fun bind(collection: CustomCollection) {
            holderView.text_collection_info_title.text = collection.title
            if (collection.bodyHtml.isBlank()) {
                holderView.text_collection_info_body_html.visibility = View.GONE
            } else {
                holderView.text_collection_info_body_html.visibility = View.VISIBLE
                holderView.text_collection_info_body_html.text = collection.bodyHtml
            }
            Glide.with(holderView.context)
                    .load(collection.imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holderView.image_collection_info)
        }
    }

    inner class CollectionDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        lateinit var collection: CustomCollection

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == VIEW_TYPE_PRODUCT) {
                val itemView = LayoutInflater.from(context)
                        .inflate(R.layout.item_product, parent, false)
                return ProductViewHolder(itemView)
            } else {
                val itemView = LayoutInflater.from(context)
                        .inflate(R.layout.item_collection_info, parent, false)
                return CollectionInfoViewHolder(itemView)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (getItemViewType(position) == VIEW_TYPE_PRODUCT) {
                (holder as ProductViewHolder).bind(collection.products[position - 1])
            } else {
                (holder as CollectionInfoViewHolder).bind(collection)
            }

        }

        override fun getItemCount(): Int {
            return collection.products.size + 1
        }

        override fun getItemViewType(position: Int): Int {
            if (position == 0) {
                return VIEW_TYPE_COLLECTION_INFO
            }
            return VIEW_TYPE_PRODUCT
        }

    }
}
