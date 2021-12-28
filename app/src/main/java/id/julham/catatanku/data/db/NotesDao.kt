package id.julham.catatanku.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import id.julham.catatanku.data.db.entities.Notes

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notes: Notes): Long

    @Update
    fun update(notes: Notes)

    @Delete
    fun delete(notes: Notes)

    @Query("select * from notes_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Notes>>


}