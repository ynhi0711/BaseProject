package com.nhinguyen.sample.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "types")
data class Type(
    @PrimaryKey
    @field:Json(name = "type_id") val typeId: String,
    @field:Json(name = "type_name") val typeName: String,
    @field:Json(name = "is_selected") var isSelected: Boolean = false
)