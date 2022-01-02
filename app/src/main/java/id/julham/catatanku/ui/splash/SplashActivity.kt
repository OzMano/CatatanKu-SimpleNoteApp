package id.julham.catatanku.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseActivity
import id.julham.catatanku.databinding.ActivitySplashBinding
import id.julham.catatanku.ui.home.HomeActivity
import id.julham.catatanku.ui.login.LoginActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity: BaseActivity<ActivitySplashBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_splash

    private lateinit var slideDownAnim: Animation

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupAppTheme()
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        //setting up anim
        slideDownAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.reveal_animation)
        binding.splashLogo.animation = slideDownAnim

        Timer("decision", false).schedule(1000) {
            if (auth.currentUser != null) {
                Intent(this@SplashActivity, HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    overridePendingTransition(
                        R.anim.fade_in_animation,
                        R.anim.fade_out_animation
                    )
                }
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                    overridePendingTransition(
                        R.anim.fade_in_animation,
                        R.anim.fade_out_animation
                    )
                }
            }
            finish()
        }
    }

    /**
     * checking shared preferences for dark mode state
     * then set the dark mode status based on shared preferences value
     */
    private fun setupAppTheme(){
        val appSettingPrefs = getSharedPreferences("AppThemeModePrefs", 0)
        val isNightModeOn = appSettingPrefs.getBoolean("DarkMode", false)

        if (!isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}