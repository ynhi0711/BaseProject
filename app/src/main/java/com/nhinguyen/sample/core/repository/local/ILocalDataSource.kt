package com.nhinguyen.sample.core.repository.local

import com.nhinguyen.sample.core.models.Item
import com.nhinguyen.sample.core.models.entity.SavedItem
import com.nhinguyen.sample.core.models.entity.Type
import com.nhinguyen.sample.core.models.response.TypeApi
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {

    suspend fun insertTypes(type: List<TypeApi>)
    fun getAllType(): Flow<List<Type>>
    fun getLocalTypesNoLiveData(): List<Type>
    fun getAllSavedItems(): Flow<List<SavedItem>>
    fun getSavedItemByItemId(itemId: String): Flow<List<SavedItem>>
    fun getSavedItemByTypeId(typeId: String): Flow<List<SavedItem>>
    suspend fun saveItem(item: Item)
    suspend fun updateItem(savedItem: SavedItem)
    suspend fun removeItem(itemId: String)
}