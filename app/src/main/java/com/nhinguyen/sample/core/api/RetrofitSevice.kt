package com.nhinguyen.sample.core.api

import com.nhinguyen.sample.core.models.response.ItemApi
import com.nhinguyen.sample.core.models.response.TypeApi
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {
    @POST("general/get_all_item_type_v2")
    suspend fun getTypes(
    ): Response<List<TypeApi>>

    @GET("general/get_item_by_type_v4")
    suspend fun getItemsByType(
        @Query("type_id") typeId: String,
        @Query("page") page: Int,
        @Query("sort") sort: String,
    ): Response<List<ItemApi>>

    @GET("MainHome/get_hot_item_v4")
    suspend fun getItemsPopularByType(
        @Query("item_type") typeId: String
    ): Response<List<ItemApi>>

    @GET("MainHome/search_items_v4")
    suspend fun searchItem(
        @Query("search_keyword") keyword: String,
        @Query("page") page: Int
    ): Response<List<ItemApi>>

    @FormUrlEncoded
    @POST("general/download_item_v1")
    suspend fun downloadItem(
        @Field("item_id") itemId: String
    ): Response<ItemApi>
}