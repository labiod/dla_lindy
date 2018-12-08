package com.sample.testapp.db

import android.provider.BaseColumns

object NoteContract {
    object NoteEntry : BaseColumns {
        const val TABLE_NAME = "notes"
        const val COLUMN_NOTE_TITLE = "title"
        const val COLUMN_NOTE_DESC = "description"
        const val COLUMN_NOTE_DATE = "note_date"
    }
}