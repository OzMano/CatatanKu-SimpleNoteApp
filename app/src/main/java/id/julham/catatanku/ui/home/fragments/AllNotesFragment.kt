package id.julham.catatanku.ui.home.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseFragment
import id.julham.catatanku.data.db.entities.Notes
import id.julham.catatanku.databinding.FragmentAllNotesBinding
import id.julham.catatanku.ui.home.EditNotesActivity
import id.julham.catatanku.ui.home.HomeActivity
import id.julham.catatanku.ui.home.adapters.AllNotesAdapter
import id.julham.catatanku.ui.splash.SplashActivity


class AllNotesFragment: BaseFragment<FragmentAllNotesBinding>(), AllNotesAdapter.RecycleClick {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_all_notes

    private lateinit var getAllNotes: LiveData<List<Notes>>
    lateinit var allNotes: List<Notes>
    lateinit var allNotesAdapter: AllNotesAdapter

    private lateinit var userUid: String
    private lateinit var collectionReference: CollectionReference


    private val TAG = "Debug014589"
//    private val TAG = "AllNotesFragmentDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, " onCreate AllNotesFragment")
        userUid = SplashActivity.auth.currentUser?.uid.toString()
        collectionReference = HomeActivity.firestoreDb.collection(userUid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, " onViewCreated AllNotesFragment")

        getAllNotes = HomeActivity.viewModel.getAllNotes()
        getAllNotes.observe(activity!!, Observer {

            //updating recyclerview
            allNotes = getAllNotes.value!!
            binding.recyclerView.layoutManager = LinearLayoutManager(activity!!)
            allNotesAdapter = AllNotesAdapter(allNotes, this)
            binding.recyclerView.adapter = allNotesAdapter
            val swipe = ItemTouchHelper(helper)
            swipe.attachToRecyclerView(binding.recyclerView)
            if (allNotes.isEmpty()) {
                binding.noNotesMessage.visibility = View.VISIBLE
            } else {
                binding.noNotesMessage.visibility = View.GONE
            }
        })


        //getting data from firestore
        populateData()
    }

    //swipe right to delete a note
    private val helper by lazy {
        object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val notes = allNotes[position]
                val id = notes.id


                val dialog = Dialog(activity!!)
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
                    HomeActivity.viewModel.delete(allNotes[position])
                    collectionReference.document("$id").delete().addOnSuccessListener {
                        Log.d(TAG, " delete success")
                    }.addOnFailureListener {
                        Log.d(TAG, " delete failure")
                    }
                    dialog.dismiss()
                }
                cancel.setOnClickListener {
                    dialog.dismiss()
                    allNotesAdapter.notifyItemChanged(viewHolder.adapterPosition)
                }

            }
        }
    }


    private fun populateData() {
        collectionReference.orderBy("id", Query.Direction.DESCENDING).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.data["id"].toString()
                        val note = document.data["note"].toString()
                        val noteDescription =
                            document.data["noteDescription"].toString()
                        val color = document.data["color"].toString()
                        val notes =
                            Notes(id.toInt(), note, noteDescription, color)
                        HomeActivity.viewModel.insert(notes)
                    }
                }
            }

    }


    //click listeners for items
    override fun onItemClick(position: Int) {
        val notes = allNotesAdapter.getNoteAt(position)

        val id = notes.id
        val note = notes.note
        val noteDescription = notes.noteDescription
        val color = notes.color


        Intent(activity!!, EditNotesActivity::class.java).also {
            it.putExtra("id", id)
            it.putExtra("note", note)
            it.putExtra("noteDescription", noteDescription)
            it.putExtra("color", color)
            startActivity(it)
            activity!!.overridePendingTransition(
                R.anim.fade_in_animation,
                R.anim.fade_out_animation
            )
        }
    }

}