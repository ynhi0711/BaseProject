package com.nhinguyen.sample.core.repository.local

import com.nhinguyen.sample.core.models.DownloadStatus
import com.nhinguyen.sample.core.models.Item
import com.nhinguyen.sample.core.models.entity.SavedItem
import com.nhinguyen.sample.core.models.entity.Type
import com.nhinguyen.sample.core.models.response.TypeApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dataDao: DataDao
) : ILocalDataSource {
    override suspend fun insertTypes(type: List<TypeApi>) {
        dataDao.insertTypes(type)
    }

    override fun getAllType(): Flow<List<Type>> =
        dataDao.getTypes()

    override fun getLocalTypesNoLiveData(): List<Type> =
        dataDao.getLocalTypesNoLiveData()

    override fun getAllSavedItems(): Flow<List<SavedItem>> = dataDao.getAllSavedItems()

    override fun getSavedItemByItemId(itemId: String): Flow<List<SavedItem>> =
        dataDao.getSavedItemByItemId(itemId)

    override fun getSavedItemByTypeId(typeId: String): Flow<List<SavedItem>> =
        dataDao.getSavedItemByTypeId(typeId)

    override suspend fun saveItem(item: Item) {
        dataDao.updateOrInsertItem(item.toSavedItem(DownloadStatus.DOWNLOADED))
    }

    override suspend fun updateItem(savedItem: SavedItem) {
        dataDao.updateItem(savedItem)
    }

    override suspend fun removeItem(itemId: String) {
        dataDao.deleteItem(itemId)
    }
}
