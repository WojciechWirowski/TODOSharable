package com.easv.dt.ww.todosharable.data.list

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BEListTodo::class], version = 1)
abstract class LocalListsDatabase: RoomDatabase() {

    abstract fun listTodoDao(): ListTodoDAO
}