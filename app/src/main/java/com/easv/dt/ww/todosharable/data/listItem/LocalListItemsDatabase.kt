package com.easv.dt.ww.todosharable.data.listItem

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BEListItem::class], version = 1)
abstract class LocalListItemsDatabase: RoomDatabase() {
    abstract fun listItemDao(): ListItemDAO
}