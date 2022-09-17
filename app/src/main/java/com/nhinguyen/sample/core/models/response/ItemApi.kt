package com.nhinguyen.sample.core.models.response

import android.os.Parcelable
import com.nhinguyen.sample.core.models.DownloadStatus
import com.nhinguyen.sample.core.models.Item
import com.nhinguyen.sample.core.models.entity.SavedItem
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemApi(
    @field:Json(name = "__v") val v: Int?,
    @field:Json(name = "_id") val id: String,
    @field:Json(name = "approved") val approved: Boolean?,
    @field:Json(name = "author_name") val authorName: String?,
    @field:Json(name = "by_nhinguyen") val bynhinguyen: Boolean?,
    @field:Json(name = "category_id") val categoryId: String?,
    @field:Json(name = "create_time") val createTime: String?,
    @field:Json(name = "created_at") val createdAt: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "download_count") val downloadCount: String?,
    @field:Json(name = "download_count_int") val downloadCountInt: Int?,
    @field:Json(name = "file_url") val fileUrl: String?,
    @field:Json(name = "hot_priority") val hotPriority: String?,
    @field:Json(name = "html_description") val htmlDescription: String?,
    @field:Json(name = "image_url") val imageUrl: String?,
    @field:Json(name = "is_verify") val isVerify: String?,
    @field:Json(name = "item_id") val itemId: String?,
    @field:Json(name = "item_name") val itemName: String?,
    @field:Json(name = "price") val price: String?,
    @field:Json(name = "priority") val priority: Int?,
    @field:Json(name = "short_description") val shortDescription: String?,
    @field:Json(name = "size") val size: String?,
    @field:Json(name = "thumb_url") val thumbUrl: String?,
    @field:Json(name = "type_id") val typeId: String?,
    @field:Json(name = "updated_at") val updatedAt: String?,
    @field:Json(name = "version") val version: String?,
    @field:Json(name = "video_code") val videoCode: String?
) : Parcelable {
    fun toSavedItem(downloadStatus: DownloadStatus) = SavedItem(
        id,
        downloadCountInt ?: 0,
        fileUrl ?: "",
        imageUrl ?: "",
        itemName ?: "",
        typeId ?: "",
        createTime ?: "",
        itemId ?: "",
        downloadCount ?: "",
        description ?: "",
        authorName ?: "",
        downloadStatus.value,
        null
    )

    fun toItem() = Item(
        id = id,
        itemId = itemId ?: "",
        itemName = itemName ?: "",
        authorName = authorName ?: "",
        typeId = typeId ?: "",
        downloadCountInt = downloadCountInt ?: 0,
        downloadCount = downloadCount ?: "",
        fileUrl = fileUrl ?: "",
        imageUrl = imageUrl ?: "",
        createTime = createTime ?: "",
        description = description ?: "",
        downloadStatus = DownloadStatus.NOT_DOWNLOAD_YET.value
    )
}