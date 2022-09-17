package com.nhinguyen.sample.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nhinguyen.sample.core.models.Item
import com.squareup.moshi.Json

@Entity(tableName = "saved_items")
class SavedItem(
    @PrimaryKey
    @field:Json(name = "_id") val id: String,
    @field:Json(name = "download_count_int") val downloadCountInt: Int,
    @field:Json(name = "file_url") val fileUrl: String,
    @field:Json(name = "image_url") val imageUrl: String,
    @field:Json(name = "item_name") val itemName: String,
    @field:Json(name = "type_id") val typeId: String,
    @field:Json(name = "create_time") val createTime: String,
    @field:Json(name = "item_id") val itemId: String,
    @field:Json(name = "download_count") val downloadCount: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "author_name") val authorName: String,
    @field:Json(name = "author_name") var downloadStatus: String,
    @field:Json(name = "file_path") var filePath: String?
) {
    fun toItem() = Item(
        id = id,
        itemId = itemId,
        itemName = itemName,
        authorName = authorName,
        typeId = typeId,
        downloadCountInt = downloadCountInt,
        downloadCount = downloadCount,
        fileUrl = fileUrl,
        imageUrl = imageUrl,
        createTime = createTime,
        description = description,
        downloadStatus = downloadStatus,
        filePath = filePath
    )
}