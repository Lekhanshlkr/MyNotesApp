package com.example.mynotesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {
    val dbName = "My Notes"
    val dbTable = "Notes"
    val dbVersion = 1
    var colID = "ID"
    var colTitle = "Title"
    var colContent = "Content"
    val createSQLTable =
        "CREATE TABLE IF NOT EXISTS $dbTable($colID INTEGER PRIMARY KEY, $colTitle TEXT, $colContent TEXT);"
    var sqlDB:SQLiteDatabase?=null

    var context:Context?=null
    constructor(context: Context){
        this.context=context
        val db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    //class for running the creating the table
    inner class DatabaseHelperNotes: SQLiteOpenHelper{

        var context:Context?=null
        constructor(context: Context):super(context,dbName,null,dbVersion){
            this.context=context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(createSQLTable)
            Toast.makeText(context,"Database is Created",Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS $dbTable")
        }

    }

    fun insertData(value:ContentValues):Long{

        val ID = sqlDB!!.insert(dbTable,"",value)
        return ID
    }

    //fun for running the sql query with given parameters
    fun queryMan(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor{
        var queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = dbTable
        val cursor = queryBuilder!!.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }

    fun deleteRow(selection:String,selectionArgs:Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }

    fun updateRow(value: ContentValues,selection: String,selectionArgs: Array<String>):Int{

        val count = sqlDB!!.update(dbTable,value,selection,selectionArgs)
        return count
    }
}