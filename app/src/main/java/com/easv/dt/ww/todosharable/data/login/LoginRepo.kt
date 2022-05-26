package com.easv.dt.ww.todosharable.data.login

class LoginRepo {
    val list:MutableList<BEUser> = ArrayList()

    init {
        list.add(BEUser(0, "user1", "password1"))
        list.add(BEUser(1, "user2", "password2"))
        list.add(BEUser(2, "user3", "password3"))
    }

    fun login(username: String, password: String): BEUser? {

        val user = list.firstOrNull { it.username == username }

        return if ( user?.password == password ) { user } else { null }

    }
}