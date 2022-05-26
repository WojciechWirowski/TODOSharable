package com.easv.dt.ww.todosharable.data.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

class ListTodoRepository private constructor(context: Context) {

    private val database: LocalListsDatabase =
        Room.databaseBuilder(context.applicationContext,
        LocalListsDatabase::class.java,
        "list-database").build()

    private val listDao = database.listTodoDao()

    fun getAll(): LiveData<List<BEListTodo>> = listDao.getAll()

    fun getById(id: Int): LiveData<BEListTodo> = listDao.getById(id)

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(f: BEListTodo) {
        executor.execute{listDao.insert(f) }
    }

    fun update(f: BEListTodo){
        executor.execute { listDao.update(f) }
    }

    fun delete(f: BEListTodo){
        executor.execute { listDao.delete(f) }
    }

    fun clear(){
        executor.execute { listDao.deleteAll() }
    }





    companion object {
        private var Instance: ListTodoRepository? = null

        fun initialize(context: Context){
            if(Instance == null)
                Instance = ListTodoRepository(context)
        }

        fun get(): ListTodoRepository {
            if(Instance != null) return Instance!!
            throw IllegalStateException("List repository not initialized")
        }
    }
}