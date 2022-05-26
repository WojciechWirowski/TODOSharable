package com.easv.dt.ww.todosharable.data.list

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListTodoDAO {

    @Query("Select * from BEListTodo order by id")
    fun getAll(): LiveData<List<BEListTodo>>

    @Query("SELECT * from BEListTodo where id = (:id)")
    fun getById(id: Int): LiveData<BEListTodo>

    @Insert
    fun insert(f: BEListTodo)

    @Update
    fun update(f: BEListTodo)

    @Delete
    fun delete(f: BEListTodo)

    @Query("DELETE from BEListTodo")
    fun deleteAll()

    @Query("SELECT * from BEListTodo order by id DESC LIMIT 1")
    fun getLastList(): LiveData<BEListTodo>
}