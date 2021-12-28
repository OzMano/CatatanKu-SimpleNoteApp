package id.julham.catatanku.ui.home.viewodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.julham.catatanku.data.db.entities.Notes
import id.julham.catatanku.data.repositories.NotesRepository

class NotesViewModel(app: Application): AndroidViewModel(app) {

    private var repository = NotesRepository(app)

    //Database operations in ViewModel
    fun insert(notes: Notes): Long? {
        return repository.insert(notes)
    }

    fun update(notes: Notes) {
        repository.update(notes)
    }

    fun delete(notes: Notes) {
        repository.delete(notes)
    }

    fun getAllNotes(): LiveData<List<Notes>> {
        return repository.getAllNotes()
    }
}