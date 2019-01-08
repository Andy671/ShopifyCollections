package com.andy671.shopifycollections.ui

import android.app.Application
import android.arch.lifecycle.*
import com.andy671.shopifycollections.data.CollectionsRepository
import com.andy671.shopifycollections.data.CustomCollection

class CollectionsViewModelFactory(val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CollectionsViewModel(application) as T
    }

}

class CollectionsViewModel(val application: Application) : ViewModel() {
    enum class Page {
        CollectionList,
        CollectionDetails
    }

    private val collectionsRepository: CollectionsRepository = CollectionsRepository(application)

    private var currentPageLiveData = MutableLiveData<CollectionsViewModel.Page>()
    private var currentCollectionLiveData = MutableLiveData<CustomCollection>()

    init {
        currentPageLiveData.value = CollectionsViewModel.Page.CollectionList
    }

    fun getCollections(): LiveData<List<CustomCollection>> {
        return collectionsRepository.getCollections()
    }

    fun getPage(): LiveData<Page> {
        return currentPageLiveData
    }

    fun getCurrentCollection(): LiveData<CustomCollection> {
        return currentCollectionLiveData
    }

    fun onClickCollectionCard(collection: CustomCollection) {
        currentCollectionLiveData.value = collection
        currentPageLiveData.value = Page.CollectionDetails
    }

    fun onBackPressed() {
        currentPageLiveData.value = Page.CollectionList
    }

}