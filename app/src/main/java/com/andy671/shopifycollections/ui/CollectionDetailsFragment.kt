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
import com.andy671.shopifycollections.R
import com.andy671.shopifycollections.data.CustomCollection
import com.andy671.shopifycollections.data.Product
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_product.view.*

class CollectionDetailsFragment : Fragment() {

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
                mListAdapter.currentList = it.products
                mListAdapter.notifyDataSetChanged()
            }
        })

        return fragmentView
    }

    inner class CollectionDetailsViewHolder(private val holderView: View) : RecyclerView.ViewHolder(holderView) {

        fun bind(product: Product) {
            holderView.text_product_title.text = product.title

            val html = resources.getString(R.string.total_available_inventory, product.totalAvailableInventory)
            holderView.text_product_total_inventory.text = Html.fromHtml(html)

            // TODO: placeholder drawable
            Glide.with(holderView.context)
                    .load(product.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
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
