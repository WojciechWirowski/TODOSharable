package com.easv.dt.ww.todosharable.data.login

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BEUser (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var username: String,
    var password: String)
    {
        override fun toString(): String {
            return "$id: Name: $username"
        }
}