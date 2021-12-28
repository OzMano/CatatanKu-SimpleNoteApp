package id.julham.catatanku.data.repositories

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import id.julham.catatanku.data.db.NotesDao
import id.julham.catatanku.data.db.NotesDatabase
import id.julham.catatanku.data.db.entities.Notes

class NotesRepository(app: Application) {

    var notesDao: NotesDao? = NotesDatabase.getDatabase(app)?.notesDao()

    fun insert(notes: Notes): Long? {
        return InsertAsync(notesDao).execute(notes).get()
    }

    fun update(notes: Notes) {
        UpdateAsync(notesDao).execute(notes)
    }

    fun delete(notes: Notes) {
        DeleteAsync(notesDao).execute(notes)
    }

    fun getAllNotes(): LiveData<List<Notes>> {
        return GetAllNotesAsync(notesDao).execute().get()
    }


    //background operations
    class InsertAsync(var notesDao: NotesDao?) : AsyncTask<Notes, Void, Long?>() {
        override fun doInBackground(vararg params: Notes): Long? {
            return notesDao?.insert(params[0])
        }
    }

    class UpdateAsync(var notesDao: NotesDao?) : AsyncTask<Notes, Void, Unit>() {
        override fun doInBackground(vararg params: Notes) {
            notesDao?.update(params[0])
        }
    }

    class DeleteAsync(var notesDao: NotesDao?) : AsyncTask<Notes, Void, Unit>() {
        override fun doInBackground(vararg params: Notes) {
            notesDao?.delete(params[0])
        }
    }

    class GetAllNotesAsync(var notesDao: NotesDao?) :
        AsyncTask<Unit, Void, LiveData<List<Notes>>>() {
        override fun doInBackground(vararg params: Unit?): LiveData<List<Notes>>? {
            return notesDao?.getAllNotes()
        }
    }


}