package com.easv.dt.ww.todosharable.data.list

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BEListTodo(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String)
{
    override fun toString(): String {
        return "$id: Name: $name"
    }
}