package id.julham.catatanku.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseActivity
import id.julham.catatanku.data.db.entities.Notes
import id.julham.catatanku.databinding.ActivityEditNotesBinding
import id.julham.catatanku.ui.splash.SplashActivity
import id.julham.catatanku.utils.NoteColorPicker

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditNotesActivity : BaseActivity<ActivityEditNotesBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_edit_notes

    private var idValue: Int? = null
    private var noteValue: String? = null
    private var noteDescriptionValue: String? = null
    private var colorValue: String? = null
    private var colorValuePublic: String? = null

    private lateinit var userUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userUid = SplashActivity.auth.currentUser?.uid.toString()

        idValue = intent.getIntExtra("id", 0)
        noteValue = intent.getStringExtra("note")
        noteDescriptionValue = intent.getStringExtra("noteDescription")
        colorValue = intent.getStringExtra("color")
        colorValuePublic = colorValue

        binding.notes.setText(noteValue)
        if (!noteDescriptionValue.equals("blank"))
            binding.notesDescription.setText(noteDescriptionValue)
        binding.notesContainerCard.setCardBackgroundColor(Color.parseColor(colorValue))

        binding.deleteNote.setOnClickListener { deleteNote() }
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    private fun deleteNote() {
        val notes = Notes(
            idValue as Int,
            noteValue as String,
            noteDescriptionValue as String,
            colorValuePublic as String
        )

        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.logout_dialog)
        dialog.setCancelable(true)
        dialog.show()

        val deleteConfirm = dialog.findViewById<Button>(R.id.log_out_confirm_btn)
        val cancel = dialog.findViewById<Button>(R.id.cancel_btn)
        val message = dialog.findViewById<TextView>(R.id.log_out_prompt)

        message.text = getString(R.string.delete_message)
        deleteConfirm.text = getString(R.string.delete)

        deleteConfirm.setOnClickListener {
            HomeActivity.viewModel.delete(notes)
            HomeActivity.firestoreDb.collection(userUid).document("$idValue").delete()
                .addOnSuccessListener { result: Any? -> }
                .addOnFailureListener { }

            dialog.dismiss()

            finish()
            overridePendingTransition(
                R.anim.fade_in_animation,
                R.anim.fade_out_animation
            )
        }

        cancel.setOnClickListener { dialog.dismiss() }
    }

    fun cardColor(view: View) {
        val id = view.id
        val colorId = NoteColorPicker.noteColor(id)
        colorValue = "#" + Integer.toHexString(ContextCompat.getColor(this, colorId))
        binding.notesContainerCard.setCardBackgroundColor(Color.parseColor(colorValue))
    }

    override fun onBackPressed() { saveNote() }

    private fun saveNote() {
        val noteValue = binding.notes.text.toString().trim()
        val noteDescriptionValue = binding.notesDescription.text.toString().trim()
        val color = colorValue.toString()

        if (noteValue.isBlank() && noteDescriptionValue.isBlank()) {
            finish()
            overridePendingTransition(
                R.anim.fade_in_animation,
                R.anim.fade_out_animation
            )
            return
        }

        //pushing to db
        val notes = Notes(idValue as Int, noteValue, noteDescriptionValue, color)

        HomeActivity.viewModel.update(notes)
        HomeActivity.firestoreDb.collection(userUid).document(idValue.toString()).set(notes)

        finish()
        overridePendingTransition(
            R.anim.fade_in_animation,
            R.anim.fade_out_animation
        )
    }
}