package com.nhinguyen.sample.core.models

import android.os.Parcelable
import com.nhinguyen.sample.core.models.entity.SavedItem
import kotlinx.android.parcel.Parcelize

@Parcelize
class Item(
    val id: String,
    val itemId: String,
    val itemName: String,
    val authorName: String,
    val typeId: String,
    var downloadCountInt: Int,
    var downloadCount: String,
    val fileUrl: String,
    val imageUrl: String,
    val createTime: String,
    val description: String,
    var downloadStatus: String,
    var filePath: String? = null
) : Parcelable {
    fun toSavedItem(downloadStatus: DownloadStatus) = SavedItem(
        id,
        downloadCountInt,
        fileUrl,
        imageUrl,
        itemName,
        typeId,
        createTime,
        itemId,
        downloadCount,
        description,
        authorName,
        downloadStatus.value,
        filePath
    )

    override fun equals(other: Any?): Boolean {
        return if (other !is Item) {
            false
        } else itemId === other.itemId
    }
}