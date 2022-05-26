package com.easv.dt.ww.todosharable

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.easv.dt.ww.todosharable.data.list.BEListTodo
import com.easv.dt.ww.todosharable.data.list.ListTodoRepository
import com.easv.dt.ww.todosharable.data.listItem.ListItemRepository
import com.easv.dt.ww.todosharable.data.login.BEUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selected: BEListTodo? = null
    private var loggedInUser: BEUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkUser()
        ListTodoRepository.initialize(this)
        ListItemRepository.initialize(this)
        setupDataObserver()
        btnAddList.setOnClickListener { addList() }
        btnDeleteList.setOnClickListener { removeList() }
        btnShowList.setOnClickListener { showList() }
        btnClearList.setOnClickListener { deleteLists() }
        btnLogOut.setOnClickListener{ logOut() }
        lvLists.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selected = parent.getItemAtPosition(position) as BEListTodo?
                tvLists.text = selected?.name
            }
    }

    private fun checkUser(){
    val userId:Int = intent.getIntExtra("id",0)
    val userName: String? = intent.getStringExtra("username")

    loggedInUser = BEUser(userId, userName!!, "NotRealPassword")
    }

    private fun addList() {

        val name = etNewListName.text.toString()

        if (name.length <= 3) {
            etNewListName.hint = "Input longer list name"
        } else {
            val repo1 = ListTodoRepository.get()
                repo1.insert(BEListTodo(0, name))
        }
        etNewListName.text.clear()
    }

    private fun logOut(){
        loggedInUser = null
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    private fun removeList() {
        if (tvLists.text.length < 3) {
            etNewListName.hint = "Cannot remove nothing"
        } else {
            val repo1 = ListTodoRepository.get()
            val repo2 = ListItemRepository.get()
            repo1.delete(BEListTodo(selected!!.id, selected!!.name))
            repo2.deleteAllListId(selected!!.id)
            selected = null
            tvLists.text = ""
        }
    }

    private fun showList() {
        val i = Intent(this, ListActivity::class.java)
        i.putExtra("list", selected?.name)
        i.putExtra("id", selected?.id)
        println("Id of selected list: " + selected?.id)
        startActivity(i)
    }

    private fun deleteLists() {
        val repo = ListTodoRepository.get()
        repo.clear()
    }

    private fun setupDataObserver() {

        val repo1 = ListTodoRepository.get()

        val getAllObserver = Observer<List<BEListTodo>> { lists ->

            val adapter = ListTodoAdapter(
                this,
                lists as ArrayList<BEListTodo>
            )
            lvLists.adapter = adapter
        }

        repo1.getAll().observe(this, getAllObserver)
    }
}

internal class ListTodoAdapter
        (context: Context, private val lists: ArrayList<BEListTodo>)
        : ArrayAdapter<BEListTodo>(context, 0,lists) {

        @SuppressLint("InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v1: View? = convertView
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.list_cell, null)
            }

            val resView: View = v1!!
            val l = lists[position]

            val name = resView.findViewById<TextView>(R.id.tvName)

            name.text = l.name

            return resView
        }
    }

