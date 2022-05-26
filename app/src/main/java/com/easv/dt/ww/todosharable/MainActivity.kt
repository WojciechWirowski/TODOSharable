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
        //initialize repositories
        ListTodoRepository.initialize(this)
        ListItemRepository.initialize(this)
        setupDataObserver()
        //set buttons and list selection listeners
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

    //set loggedInUser
    private fun checkUser(){
    val userId:Int = intent.getIntExtra("id",0)
    val userName: String? = intent.getStringExtra("username")

    loggedInUser = BEUser(userId, userName!!, "NotRealPassword")
    }


    //add list
    //if list name input is shorter than 3 letters list will not be created
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

    //returns to LoginActivity and set user as null
    private fun logOut(){
        loggedInUser = null
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    //remove selected list if its name is not shorter than 3 characters
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

    //opens Detailed view ListActivity with extras: id and list
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


    //setting up data observer for lists and displaying them on screen using custom list adapter
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

//ListTodoAdapter set lvLists to given list of BEListTodo
//use list_cell.xml to create list items
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

