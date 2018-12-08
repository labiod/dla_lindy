package com.sample.testapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class SQLDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_NOTES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_NOTES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "notes.db"
        const val DATABASE_VERSION = 1
        const val SQL_CREATE_NOTES =
            "CREATE TABLE ${NoteContract.NoteEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${NoteContract.NoteEntry.COLUMN_NOTE_TITLE} TEXT," +
                "${NoteContract.NoteEntry.COLUMN_NOTE_DESC} TEXT," +
                "${NoteContract.NoteEntry.COLUMN_NOTE_DATE} TIMESTAMP)"
        const val SQL_DELETE_NOTES = "DROP TABLE IF EXISTS ${NoteContract.NoteEntry.TABLE_NAME}"

    }
}