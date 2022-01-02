package id.julham.catatanku.ui.home.fragments

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseFragment
import id.julham.catatanku.data.db.entities.Notes
import id.julham.catatanku.databinding.FragmentAddNotesBinding
import id.julham.catatanku.ui.home.HomeActivity
import id.julham.catatanku.ui.splash.SplashActivity
import id.julham.catatanku.utils.NoteColorPicker
import id.julham.catatanku.utils.hideKeyboard

class AddNotesFragment : BaseFragment<FragmentAddNotesBinding>(), View.OnClickListener {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_add_notes

    private val TAG = "AddNotesFragmentDebug"

    private lateinit var userUid: String

    private var cardColor: String? = null
    private var colorId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, " onCreate AddNotesFragment")
        userUid = SplashActivity.auth.currentUser?.uid.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, " onViewCreated AddNotesFragment")
        
        binding.one.setOnClickListener(this)
        binding.two.setOnClickListener(this)
        binding.three.setOnClickListener(this)
        binding.four.setOnClickListener(this)
        binding.five.setOnClickListener(this)
        binding.six.setOnClickListener(this)
        binding.seven.setOnClickListener(this)
        binding.eight.setOnClickListener(this)

        //setting default card color
        colorId = NoteColorPicker.noteColor(randomColor().id)
        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(activity!!, colorId!!))
        binding.notesContainerCard.setCardBackgroundColor(Color.parseColor(cardColor))

        binding.btnSave.setOnClickListener {  saveNote() }
    }

    private fun randomColor(): ImageButton {
        return when ((1..8).random()) {
            1 -> binding.one
            2 -> binding.two
            3 -> binding.three
            4 -> binding.four
            5 -> binding.five
            6 -> binding.six
            7 -> binding.seven
            8 -> binding.eight
            else -> binding.two
        }
    }

    /**
     * save note to local db
     * and pushing to firestoreDb
     */
    private fun saveNote() {
        val noteString = binding.notes.text.toString().trim()
        val noteDescriptionString = binding.notesDescription.text.toString().trim()
        val color = cardColor.toString()

        if (noteString.isBlank() && noteDescriptionString.isBlank())
            return

        binding.btnSave.isEnabled = false
        binding.btnSave.text = getString(R.string.saving)

        val roomId = (System.currentTimeMillis() / 1000).toInt()

        //pushing to db
        val notes = Notes(roomId, noteString, noteDescriptionString, color)

        HomeActivity.viewModel.insert(notes)?.let { id ->
            val notesToFirebase = Notes(id.toInt(), noteString, noteDescriptionString, color)

            //pushing to firestore
            HomeActivity.firestoreDb.collection(userUid)
                .document(id.toString())
                .set(notesToFirebase)
                .addOnSuccessListener { result: Any? ->
                    Log.d(TAG, " Firestore success")
                }.addOnFailureListener {
                    Log.d(TAG, " Firestore error ${it.message}")
                }

            binding.btnSave.text = getString(R.string.saved)

            Handler().postDelayed({
                binding.btnSave.isEnabled = true
                binding.btnSave.text = getString(R.string.save)
                binding.notes.text.clear()
                binding.notesDescription.text.clear()
                hideKeyboard()
            }, 1000)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.one -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.two -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.three -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.four -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.five -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.six -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.seven -> colorId = NoteColorPicker.noteColor(v.id)
            R.id.eight -> colorId = NoteColorPicker.noteColor(v.id)
        }

        cardColor = "#" + Integer.toHexString(ContextCompat.getColor(activity!!, colorId!!))
        binding.notesContainerCard.setCardBackgroundColor(Color.parseColor(cardColor))
    }
}