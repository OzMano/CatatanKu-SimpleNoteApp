package id.julham.catatanku.ui.home.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import fr.castorflex.android.circularprogressbar.CircularProgressBar
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseFragment
import id.julham.catatanku.databinding.FragmentSettingsBinding
import id.julham.catatanku.ui.splash.SplashActivity
import id.julham.catatanku.utils.AppThemeMode

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_settings

    private var isDarkModeOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appSettingPrefs = activity!!.getSharedPreferences("AppThemeModePrefs", 0)
        isDarkModeOn = appSettingPrefs.getBoolean("DarkMode", false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.user.append(" ${SplashActivity.auth.currentUser?.displayName}")

        binding.logOutBtn.setOnClickListener {
            val dialog = Dialog(requireActivity())
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.logout_dialog)
            dialog.setCancelable(true)
            dialog.show()

            val logOut = dialog.findViewById<Button>(R.id.log_out_confirm_btn)
            val cancel = dialog.findViewById<Button>(R.id.cancel_btn)

            logOut.setOnClickListener {
                SplashActivity.auth.signOut()
                dialog.dismiss()

                Intent(activity, SplashActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    activity!!.overridePendingTransition(
                        R.anim.fade_in_animation,
                        R.anim.fade_out_animation
                    )
                }
            }

            cancel.setOnClickListener { dialog.dismiss() }
        }

        binding.aboutBtn.setOnClickListener {
            val dialog = Dialog(requireActivity())
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.about_dialog)
            dialog.setCancelable(true)
            dialog.show()
        }

        binding.privacyPolicyBtn.setOnClickListener {
            val dialog = Dialog(activity!!)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.privacy_policy_dialog)
            dialog.setCancelable(true)
            dialog.show()

            val webView = dialog.findViewById<WebView>(R.id.web_view)
            val progressBar = dialog.findViewById<CircularProgressBar>(R.id.progress_bar)

            webView.loadUrl("https://github.com/OzMano/")

            val handler = Handler()
            handler.postDelayed({
                progressBar.visibility = View.GONE
                webView.visibility = View.VISIBLE
            }, 3000)
        }

        //setting up the mode switch
        binding.modeSwitch.isChecked = isDarkModeOn
        binding.modeSwitch.setOnCheckedChangeListener { _: CompoundButton, isNightModeOnFlag: Boolean ->
            AppThemeMode(isNightModeOnFlag, activity!!).setTheme()
        }

        binding.contactBtn.setOnClickListener { sendMail() }
    }

    private fun sendMail() {
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("julhamadhiputera@mail.com"))
        emailIntent.selector = selectorIntent

        activity!!.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
}