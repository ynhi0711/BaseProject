package com.nhinguyen.sample.core.repository.remote

import com.nhinguyen.sample.core.api.BaseDataSource
import com.nhinguyen.sample.core.api.RetrofitService
import com.nhinguyen.sample.core.models.response.ItemApi
import com.nhinguyen.sample.core.models.response.TypeApi
import com.nhinguyen.sample.core.data.Result
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: RetrofitService
) : BaseDataSource(), IRemoteDataSource {

    override suspend fun getItemsByType(
        typeId: String,
        page: Int,
        sortType: String
    ): Result<List<ItemApi>> {
        return getResult { service.getItemsByType(typeId, page, sortType) }
    }

    override suspend fun getItemsPopularByType(typeId: String): Result<List<ItemApi>> {
        return getResult { service.getItemsPopularByType(typeId) }
    }

    override suspend fun getTypes(): Result<List<TypeApi>> {
        return getResult { service.getTypes() }
    }

    override suspend fun searchItem(keyword: String, page: Int): Result<List<ItemApi>> {
        return getResult { service.searchItem(keyword, page) }
    }

    override suspend fun downloadItem(itemId: String): Result<ItemApi> {
        return getResult { service.downloadItem(itemId) }
    }
}