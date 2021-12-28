package id.julham.catatanku.ui.home.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import id.julham.catatanku.R
import id.julham.catatanku.data.db.entities.Notes
import java.text.SimpleDateFormat
import java.util.*

class AllNotesAdapter(val notes: List<Notes>, val recycleClick: RecycleClick) :
    RecyclerView.Adapter<AllNotesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.notes_item, parent, false)
        return ViewHolder(view)

    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val notes = notes[position]


        if (notes.note == ""){
            holder.notesItem.text = notes.noteDescription
        }else{
            holder.notesItem.text = notes.note
        }

        val timestamp = notes.id.toLong() * 1000
        val dateTimeTemp = Date(timestamp)
        val format = SimpleDateFormat("h:mm a dd.MM.yyyy")
        val dateTime = format.format(dateTimeTemp)

        var writtenAtString = holder.itemView.context.getString(R.string.written_at)
        writtenAtString = "$writtenAtString $dateTime"
        holder.timestampItem.text = writtenAtString

        holder.notesContainerCard.setCardBackgroundColor(Color.parseColor(notes.color))
    }

    override fun getItemCount() = notes.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var notesItem: TextView
        var timestampItem: TextView
        var notesContainerCard: CardView

        init {
            view.setOnClickListener {
                recycleClick.onItemClick(adapterPosition)
            }
            this.notesItem = view.findViewById(R.id.notes_item)
            this.timestampItem = view.findViewById(R.id.timestamp_item)
            this.notesContainerCard = view.findViewById(R.id.notes_container_card)
        }
    }


    interface RecycleClick {
        fun onItemClick(position: Int)
    }

    fun getNoteAt(position: Int): Notes {
        return notes[position]
    }
}