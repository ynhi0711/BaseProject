package com.nhinguyen.sample.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nhinguyen.sample.core.models.entity.SavedItem
import com.nhinguyen.sample.core.models.entity.Type
import com.squareup.moshi.Moshi
import com.nhinguyen.sample.core.repository.local.DataDao

/**
 * Database schema used by the App
 */
@Database(
    entities = [
        Type::class,
        SavedItem::class
    ],
    version = 3,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    companion object {
        lateinit var moshi: Moshi
    }

    abstract fun gameDao(): DataDao
}