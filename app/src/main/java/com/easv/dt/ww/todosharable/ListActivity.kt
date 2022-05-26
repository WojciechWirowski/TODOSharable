package com.easv.dt.ww.todosharable

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.easv.dt.ww.todosharable.data.listItem.BEListItem
import com.easv.dt.ww.todosharable.data.listItem.ListItemRepository
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    private var selected: BEListItem? = null
    private var listName = ""
    private var listId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        intentExtras()
        //initialize repository
        ListItemRepository.initialize(this)
        setupDataObserver()
        //set buttons and list selection listeners
        btnAddListItem.setOnClickListener { addListItem() }
        btnDeleteListItem.setOnClickListener { deleteListItem() }
        btnCheckListItem.setOnClickListener { checkListItem() }
        btnCamera.setOnClickListener { openCamera() }
        btnSMSIntent.setOnClickListener { sendSMS() }
        lvListItems.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selected = parent.getItemAtPosition(position) as BEListItem?
            }

    }

    //setting up data observer for listItems ( with specific listId ) and displaying it on screen using custom list adapter
    private fun setupDataObserver() {
        val repo = ListItemRepository.get()
        val getAllObserver = Observer<List<BEListItem>>{ lists ->
            val adapter = ListItemAdapter(
                this,
                lists as ArrayList<BEListItem>
            )
            lvListItems.adapter = adapter
        }
        repo.getByListId(listId).observe(this, getAllObserver)
    }

    //get intent data from MainActivity containing list name, listId and set its value to tvListName field and listId
    private fun intentExtras(){
        listName = intent.getStringExtra("list").toString()
        listId = intent.getIntExtra("id", 0)
        tvListName.text = listName
        println(listName)
    }

//add listItem to list with provided listId
    private fun addListItem(){
        val repo = ListItemRepository.get()
        val text = etNewListItemText.text.toString()

        if (text.length <= 3) {
            etNewListItemText.hint = "Input longer list name"
        } else {
            repo.insert(BEListItem(id = 0, etNewListItemText.text.toString(), listId, false))
        }

        etNewListItemText.text.clear()
    }

    //delete list item with id equal selected.id
    private fun deleteListItem(){
        val repo = ListItemRepository.get()
        selected?.let { repo.delete(it) }
    }

    //sets done value of selected item to opposite
    private fun checkListItem(){
        val repo = ListItemRepository.get()
        val item = selected?.let { BEListItem(it.id, selected!!.text, selected!!.listId, !selected!!.done) }
        if (item != null) {
            repo.update(item)
        }else{
            println("Cannot check null item exception")
        }
    }

    //opens Camera activity
    private fun openCamera(){
        val i = Intent(this, Camera::class.java)
        startActivity(i)
    }

    //using SMS intent sending message( Item selected item text done = selected item done true/false ) to etPhoneNumber
    private fun sendSMS() {
        if (selected != null && etPhoneNumber.text.length >= 8) {
            val phoneNumber = etPhoneNumber.text
            val message = "Item ${selected?.text} done = ${selected?.done}"

            println("sending SMS message to: $phoneNumber text: $message")
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber.toString(), null, message, null, null)
        }
    }
}

//ListItemAdapter set lvListItems to given list of BEListItem
//use list_cell.xml to create list items
internal class ListItemAdapter
    (context: Context, private val lists: ArrayList<BEListItem>)
    : ArrayAdapter<BEListItem>(context, 0,lists) {

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
        if(!l.done){
            resView.setBackgroundColor(Color.parseColor("#ff0000"))
        }
        else{
            resView.setBackgroundColor(Color.parseColor("#00ff00"))
        }

        name.text = l.text

        return resView
    }
}