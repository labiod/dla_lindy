package com.sample.testapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.sample.testapp.db.NoteContract
import com.sample.testapp.db.SQLDBHelper
import kotlinx.android.synthetic.main.kalendarz.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {
    private lateinit var adapter : ArrayAdapter<String>

    private lateinit var task: NoteLoadTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kalendarz)
        calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendarView.date = calendar.timeInMillis

            val dbhelper = SQLDBHelper(applicationContext)
            val db = dbhelper.readableDatabase
            task = NoteLoadTask(calendar.timeInMillis, adapter)
            task.execute(db)

        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        note_list.adapter = adapter
    }

    fun clickButton(view:View) {
        val date = calendarView.date
        val intent = Intent(this,NoteActivity::class.java)
        intent.putExtra("current_date", date)
        startActivity(intent)

    }

    override fun onResume() {
        super.onResume()
        val dbhelper = SQLDBHelper(applicationContext)
        val db = dbhelper.readableDatabase
        task = NoteLoadTask(calendarView.date, adapter)
        task.execute(db)
    }

    class NoteLoadTask(val date: Long, val adapter: ArrayAdapter<String>) : AsyncTask<SQLiteDatabase, Unit, List<String>>() {
        override fun doInBackground(vararg db: SQLiteDatabase): List<String> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date

            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startTime = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endTime = calendar.timeInMillis
            val columns = arrayOf(
                NoteContract.NoteEntry.COLUMN_NOTE_TITLE
            )
            val selection = "${NoteContract.NoteEntry.COLUMN_NOTE_DATE} BETWEEN $startTime AND $endTime"
            val cursor = db[0].query(
                NoteContract.NoteEntry.TABLE_NAME,
                columns,
                selection,
                null,
                null,
                null,
                null)
            val notes = ArrayList<String>()
            if (cursor.moveToFirst()) {
                while(cursor.moveToNext()) {
                    notes.add(cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NOTE_TITLE)))
                }
            }
            cursor.close()
            return notes
        }

        override fun onPostExecute(result: List<String>) {
            adapter.clear()
            adapter.addAll(result)
            adapter.notifyDataSetChanged()
        }
    }
}
