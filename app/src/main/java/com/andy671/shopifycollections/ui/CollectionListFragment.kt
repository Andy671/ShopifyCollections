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
import android.widget.ProgressBar
import com.andy671.shopifycollections.R
import com.andy671.shopifycollections.data.CustomCollection
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection.view.*


class CollectionListFragment : Fragment() {

    private lateinit var mViewModel: CollectionsViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mListAdapter: CollectionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_collection_list, container, false)

        activity?.run {
            mViewModel = ViewModelProviders.of(this, CollectionsViewModelFactory(application))
                    .get(CollectionsViewModel::class.java)
        } ?: throw Exception("Wrong Activity")

        mListAdapter = CollectionListAdapter()

        mRecyclerView = fragmentView.findViewById<RecyclerView>(R.id.recycler_collection_list)
        mRecyclerView.adapter = mListAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mViewModel.getCollections().observe(this, Observer {
            mListAdapter.currentList = it as ArrayList<CustomCollection>
            mListAdapter.notifyDataSetChanged()
            if (mListAdapter.currentList.size > 0) {
                fragmentView.findViewById<ProgressBar>(R.id.progress_bar_list).visibility = View.GONE
                mRecyclerView.visibility = View.VISIBLE
            } else {
                fragmentView.findViewById<ProgressBar>(R.id.progress_bar_list).visibility = View.VISIBLE
                mRecyclerView.visibility = View.GONE
            }
        })

        return fragmentView
    }

    inner class CollectionListViewHolder(private val holderView: View) : RecyclerView.ViewHolder(holderView) {

        fun bind(collection: CustomCollection, position: Int) {
            holderView.card_collection.setOnClickListener {
                mViewModel.onClickCollectionCard(position)
            }
            holderView.text_collection_title.text = collection.title

            Glide.with(holderView.context)
                    .load(collection.imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holderView.image_collection)
        }
    }

    inner class CollectionListAdapter : RecyclerView.Adapter<CollectionListViewHolder>() {

        var currentList: ArrayList<CustomCollection> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionListViewHolder {
            val itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_collection, parent, false)
            return CollectionListViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CollectionListViewHolder, position: Int) {
            holder.bind(currentList[position], position)
        }

        override fun getItemCount(): Int {
            return currentList.size
        }
    }


}


