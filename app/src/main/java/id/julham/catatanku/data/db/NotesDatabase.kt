package id.julham.catatanku.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.julham.catatanku.data.db.entities.Notes

//Database Class
@Database(entities = [Notes::class], version = 3)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {
        var instance: NotesDatabase? = null
        fun getDatabase(context: Context): NotesDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, NotesDatabase::class.java,
                    "notes_database"
                ).build()
            }
            return instance
        }
    }
}