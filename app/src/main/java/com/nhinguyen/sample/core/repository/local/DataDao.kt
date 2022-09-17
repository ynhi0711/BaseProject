package com.nhinguyen.sample.core.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nhinguyen.sample.core.models.entity.SavedItem
import com.nhinguyen.sample.core.models.entity.Type
import com.nhinguyen.sample.core.models.response.TypeApi
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Transaction
    @Query("SELECT * FROM types")
    fun getTypes(): Flow<List<Type>>

    @Transaction
    @Query("SELECT * FROM types")
    fun getLocalTypesNoLiveData(): List<Type>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertType(order: Type): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateType(order: Type?)

    @Transaction
    suspend fun insertTypes(typeApi: List<TypeApi>?) {
        deleteTypes()
        typeApi?.forEach {
            updateOrInsertType(it.toTypeModel())
        }
    }

    @Transaction
    suspend fun updateOrInsertType(order: Type) {
        val i = insertType(order)
        if (i == -1L) updateType(order)
    }

    @Transaction
    @Query("DELETE FROM types")
    suspend fun deleteTypes()

    @Transaction
    @Query("DELETE FROM saved_items WHERE itemId == :itemId")
    suspend fun deleteItem(itemId: String)

    @Transaction
    @Query("SELECT * FROM saved_items")
    fun getAllSavedItems(): Flow<List<SavedItem>>

    @Transaction
    @Query("SELECT * FROM saved_items WHERE itemId = :itemId")
    fun getSavedItemByItemId(itemId: String): Flow<List<SavedItem>>

    @Transaction
    @Query("SELECT * FROM saved_items WHERE typeId = :typeId")
    fun getSavedItemByTypeId(typeId: String): Flow<List<SavedItem>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(savedItem: SavedItem): Long

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(savedItem: SavedItem?)

    @Transaction
    suspend fun insertItems(savedItems: List<SavedItem>?) {
        savedItems?.forEach {
            updateOrInsertItem(it)
        }
    }

    @Transaction
    suspend fun updateOrInsertItem(savedItem: SavedItem) {
        val i = insertItem(savedItem)
        if (i == -1L) updateItem(savedItem)
    }
}