package com.nhinguyen.sample.core.repository.remote

import com.nhinguyen.sample.core.models.response.ItemApi
import com.nhinguyen.sample.core.models.response.TypeApi
import com.nhinguyen.sample.core.data.Result

interface IRemoteDataSource {
    suspend fun getTypes(): Result<List<TypeApi>>
    suspend fun getItemsByType(typeId: String, page: Int, sortType: String): Result<List<ItemApi>>
    suspend fun getItemsPopularByType(typeId: String): Result<List<ItemApi>>
    suspend fun searchItem(keyword: String, page: Int): Result<List<ItemApi>>
    suspend fun downloadItem(itemId: String): Result<ItemApi>
}