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

    private var currentPage = MutableLiveData<CollectionsViewModel.Page>()
    private var currentCollectionIndex = MutableLiveData<Int>()

    private val currentCollection: LiveData<CustomCollection?> = Transformations.switchMap(currentCollectionIndex) { index ->
        Transformations.map(getCollections()) {
            if (!it.isEmpty()) {
                it[index]
            } else {
                null
            }
        }
    }

    init {
        currentPage.value = CollectionsViewModel.Page.CollectionList
        collectionsRepository.retrieveCustomCollections()
    }

    fun getCollections(): LiveData<List<CustomCollection>> {
        return collectionsRepository.getCollections()
    }

    fun getPage(): LiveData<Page> {
        return currentPage
    }

    fun getCurrentCollection(): LiveData<CustomCollection?> {
        return currentCollection
    }

    fun onClickCollectionCard(position: Int) {
        currentCollectionIndex.value = position
        collectionsRepository.retrieveCollectionProducts(position)
        currentPage.value = Page.CollectionDetails
    }

    fun onBackPressed() {
        currentPage.value = Page.CollectionList
    }

}