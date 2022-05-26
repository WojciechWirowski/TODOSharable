package com.easv.dt.ww.todosharable.data.listItem

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BEListItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var text: String,
    var listId: Int,
    var done: Boolean = false)
{
    override fun toString(): String {
        return "$id: Name: $text"
    }
}
