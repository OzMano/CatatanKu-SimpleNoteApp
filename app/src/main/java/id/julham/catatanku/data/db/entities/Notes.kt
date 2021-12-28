package id.julham.catatanku.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val note: String,
    val noteDescription: String,
    val color: String
) {
}