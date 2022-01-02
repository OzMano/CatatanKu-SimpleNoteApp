package id.julham.catatanku.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseActivity
import id.julham.catatanku.databinding.ActivityHomeBinding
import id.julham.catatanku.ui.home.adapters.HomePagerAdapter
import id.julham.catatanku.ui.home.viewodel.NotesViewModel
import id.julham.catatanku.utils.hideKeyboard

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_home

    private lateinit var homePagerAdapter: HomePagerAdapter

    companion object {
        lateinit var viewModel: NotesViewModel
        lateinit var firestoreDb: FirebaseFirestore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        //firebase firestore
        firestoreDb = FirebaseFirestore.getInstance()
        val firebaseSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestoreDb.firestoreSettings = firebaseSettings

        //setting up HomePager
        homePagerAdapter = HomePagerAdapter(supportFragmentManager)
        binding.homePager.adapter = homePagerAdapter
        binding.homePager.offscreenPageLimit = 3

        binding.homePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.bnMain.menu.getItem(position).isChecked = true
            }
        })

        binding.homePager.currentItem = 1

        binding.bnMain.setOnItemSelectedListener { item ->
            hideKeyboard()

            when (item.itemId) {
                R.id.all_notes -> binding.homePager.currentItem = 0
                R.id.add_notes -> binding.homePager.currentItem = 1
                R.id.settings -> binding.homePager.currentItem = 2
            }

            true
        }
    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.logout_dialog)
        dialog.setCancelable(true)
        dialog.show()

        val prompt = dialog.findViewById<TextView>(R.id.log_out_prompt)
        val exit = dialog.findViewById<Button>(R.id.log_out_confirm_btn)
        val cancel = dialog.findViewById<Button>(R.id.cancel_btn)

        prompt.text = getString(R.string.exit_prompt)
        exit.text = getString(R.string.exit)

        exit.setOnClickListener {
            dialog.dismiss()
            super.onBackPressed()
        }

        cancel.setOnClickListener { dialog.dismiss() }
    }
}