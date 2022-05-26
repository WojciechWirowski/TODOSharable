package com.easv.dt.ww.todosharable.data.listItem

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListItemDAO {

    @Query("Select * from BEListItem order by id")
    fun getAll(): LiveData<List<BEListItem>>

    @Query("SELECT * from BEListItem where listId = (:listId) order by id")
    fun getByListId(listId: Int): LiveData<List<BEListItem>>

    @Query("SELECT * from BEListItem where id = (:id)")
    fun getById(id: Int): LiveData<BEListItem>

    @Insert
    fun insert(f: BEListItem)

    @Update
    fun update(f: BEListItem)

    @Delete
    fun delete(f: BEListItem)

    @Query("DELETE from BEListItem")
    fun deleteAll()

    @Query("DELETE from BEListItem where listId = (:listId)")
    fun deleteAllListId(listId: Int)
}