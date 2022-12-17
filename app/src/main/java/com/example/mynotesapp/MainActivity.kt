 package com.example.mynotesapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note_ticket.view.*

 class MainActivity : AppCompatActivity() {

    var listOfNotes = ArrayList<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //load from Database
         loadQuery("%") // get all rows

    }

     override fun onResume() {
         super.onResume()
         loadQuery("%")
     }
     @SuppressLint("Range")
     //fun for loading the sql query
     fun loadQuery(title:String){

         listOfNotes.clear()
         val dbManager = DbManager(this)
         val cursor = dbManager.queryMan(arrayOf("ID","Title","Content"),"Title like ?", arrayOf(title),"Title")
         if(cursor.moveToFirst()){
             do{
                 val id = cursor.getInt(cursor.getColumnIndex("ID"))
                 val title = cursor.getString(cursor.getColumnIndex("Title"))
                 val content = cursor.getString(cursor.getColumnIndex("Content"))

                 listOfNotes.add(Note(id,title,content))
             }while(cursor.moveToNext())
         }
         val adapter = MyNotesAdapter(this,listOfNotes)
         lvNotes.adapter = adapter

     }

     // implementing the menu buttons
     override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         menuInflater.inflate(R.menu.main_menu,menu)

         val searchV = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
         val searchMan = getSystemService(Context.SEARCH_SERVICE) as SearchManager
         searchV.setSearchableInfo(searchMan.getSearchableInfo(componentName))
         //implementing the search button functions
         searchV.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
             override fun onQueryTextSubmit(p0: String?): Boolean {
                 loadQuery("%$p0%")
                 return false
             }

             override fun onQueryTextChange(p0: String?): Boolean {
                 loadQuery("%$p0%")
                 return false
             }

         })

         return super.onCreateOptionsMenu(menu)
     }

     //implementing the addNote item button
     override fun onOptionsItemSelected(item: MenuItem): Boolean {

         if (item.itemId == R.id.addNote) {
             var intent = Intent(this,AddNotes::class.java)
             startActivity(intent)
         }

         return super.onOptionsItemSelected(item)
     }

     // adapter for ticket
    inner class MyNotesAdapter: BaseAdapter {

        var notesList = ArrayList<Note>()
         var context:Context?=null
        constructor(context: Context,noteList:ArrayList<Note>){
            this.notesList = noteList
            this.context=context
        }

        override fun getCount(): Int {
            return notesList.size
        }

        override fun getItem(p0: Int): Any {
            return notesList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(index: Int, view: View?, p2: ViewGroup?): View {
            val curNote = notesList[index]
            var myView = layoutInflater.inflate(R.layout.note_ticket,null)

            myView.tvTitle.text = curNote.title.toString()
            myView.tvContent.text = curNote.content.toString()

            myView.ivDelete.setOnClickListener{
                val dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf( curNote.nodeID.toString())
                dbManager.deleteRow("ID=?", selectionArgs)
                loadQuery("%")
            }

            myView.ivEdit.setOnClickListener {
                goToUpdate(curNote)
            }

            return myView
        }
    }

     fun goToUpdate(note:Note){
         var intent = Intent(this,AddNotes::class.java)
         intent.putExtra("ID",note.nodeID)
         intent.putExtra("Title",note.title)
         intent.putExtra("Content",note.content)
         startActivity(intent)
     }
}