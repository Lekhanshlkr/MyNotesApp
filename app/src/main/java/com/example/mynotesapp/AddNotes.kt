 package com.example.mynotesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

 class AddNotes : AppCompatActivity() {

     var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try{
            var bundle:Bundle = intent.extras!!
            id = bundle.getInt("ID",0)
            if(id!=0) {
                etTitle.setText(bundle.getString("Title"))
                etContent.setText(bundle.getString("Content"))
            }
        }catch (ex:java.lang.Exception){}
    }

    fun buAdd(view: View){
        val dbManager = DbManager(this)
        val values = ContentValues()
        values.put("Title",etTitle.text.toString())
        values.put("Content",etContent.text.toString())

        if(id==0){
            val ID = dbManager.insertData(values)
            if(ID>0){
                Toast.makeText(this,"Note Added Successfully",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"Cannot Add the Note",Toast.LENGTH_LONG).show()
            }
        }else{
            val ID = dbManager.updateRow(values,"ID=?", arrayOf(id.toString()))
            if(ID>0){
                Toast.makeText(this,"Note Updated Successfully",Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(this,"Cannot Update the Note",Toast.LENGTH_LONG).show()
            }
        }
    }
}