package com.easv.dt.ww.todosharable.data.listItem

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class ListItemRepository private constructor(context: Context) {

    private val database: LocalListItemsDatabase =
        Room.databaseBuilder(context.applicationContext,
        LocalListItemsDatabase::class.java,
        "list-items-database").build()

    private val listItemDao = database.listItemDao()

    fun getAll(): LiveData<List<BEListItem>> = listItemDao.getAll()

    fun getByListId(id: Int): LiveData<List<BEListItem>> = listItemDao.getByListId(id)

    fun getById(id: Int) = listItemDao.getById(id)

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(f: BEListItem){
        executor.execute{listItemDao.insert(f)}
    }

    fun update(f: BEListItem){
        executor.execute { listItemDao.update(f) }
    }

    fun delete(f: BEListItem){
        executor.execute { listItemDao.delete(f) }
    }

    fun clear(){
        executor.execute { listItemDao.deleteAll() }
    }
    fun deleteAllListId(listId: Int){
        executor.execute { listItemDao.deleteAllListId(listId) }
    }

    companion object {

        private var Instance: ListItemRepository? = null

        fun initialize(context: Context){
            if(Instance == null)
                Instance = ListItemRepository(context)
        }

        fun get(): ListItemRepository {
            if(Instance != null) return Instance!!
            throw IllegalStateException("ListItem repository not initialized")
        }
    }
}