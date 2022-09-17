package com.nhinguyen.sample.core.repository

import androidx.lifecycle.asLiveData
import com.nhinguyen.sample.core.data.resultFlowNoCache
import com.nhinguyen.sample.core.data.resultLiveData
import com.nhinguyen.sample.core.data.resultLiveDataNoCache
import com.nhinguyen.sample.core.models.Item
import com.nhinguyen.sample.core.models.entity.SavedItem
import com.nhinguyen.sample.core.repository.local.LocalDataSource
import com.nhinguyen.sample.core.repository.remote.IRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class Repository @Inject constructor(
    private val remoteSource: IRemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun getTypes() = resultLiveData(
        databaseQuery = { localDataSource.getAllType().asLiveData() },
        saveCallResult = { localDataSource.insertTypes(it) },
        networkCall = { remoteSource.getTypes() }
    )

    fun getItemsByType(typeId: String, page: Int, sortType: String) =
        resultLiveDataNoCache { remoteSource.getItemsByType(typeId, page, sortType) }

    fun searchItem(keyword: String, page: Int) =
        resultLiveDataNoCache { remoteSource.searchItem(keyword, page) }

    fun downloadItem(itemId: String) =
        resultLiveDataNoCache { remoteSource.downloadItem(itemId) }

    suspend fun getPopularItemsByType(typeId: String) = resultFlowNoCache(
        networkCall = { remoteSource.getItemsPopularByType(typeId) },
        emitLoading = false
    )

    fun getLocalTypes() = localDataSource.getAllType()
    fun getLocalTypesNoLiveData() = localDataSource.getLocalTypesNoLiveData()

    fun getAllSavedItem() = localDataSource.getAllSavedItems()
    fun getSavedItemByItemId(itemId: String) = localDataSource.getSavedItemByItemId(itemId)
    fun getSavedItemByTypeId(typeId: String) = localDataSource.getSavedItemByTypeId(typeId)
    suspend fun saveItemDownload(item: Item) = localDataSource.saveItem(item)
    suspend fun updateItem(savedItem: SavedItem) = localDataSource.updateItem(savedItem)
    suspend fun removeSavedItem(itemId: String) = localDataSource.removeItem(itemId)
}