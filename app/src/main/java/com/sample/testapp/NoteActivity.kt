package com.sample.testapp

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sample.testapp.db.NoteContract
import com.sample.testapp.db.SQLDBHelper
import kotlinx.android.synthetic.main.note_layout.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : Activity() {
    private var date: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_layout)
        date = intent.getLongExtra("current_date", 0)
        val dateFormat = SimpleDateFormat("EEE, MMM d, ''yy", Locale.getDefault())
        val dateText = dateFormat.format(Date(date))
        title = dateText
    }

    fun method1(view: View) {
        save()
        finish()
    }

    fun method2(view: View) {
        finish()
    }

    private fun save() {
        val dbhelper = SQLDBHelper (this)
        val values = ContentValues()
        values.put(NoteContract.NoteEntry.COLUMN_NOTE_TITLE, titleText.text.toString())
        values.put (NoteContract.NoteEntry.COLUMN_NOTE_DESC, note_desc.text.toString())
        values.put (NoteContract.NoteEntry.COLUMN_NOTE_DATE, date)
        val db = dbhelper.writableDatabase
        db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values)
        Toast.makeText(this, "Note is saved", Toast.LENGTH_SHORT).show()
    }
}
