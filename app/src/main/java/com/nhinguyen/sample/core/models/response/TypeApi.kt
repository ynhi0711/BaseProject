package com.nhinguyen.sample.core.models.response

import com.nhinguyen.sample.core.models.entity.Type
import com.squareup.moshi.Json

data class TypeApi(
    @field:Json(name = "type_id") val typeId: String,
    @field:Json(name = "type_name") val typeName: String,
) {
    fun toTypeModel() = Type(typeId, typeName)
}