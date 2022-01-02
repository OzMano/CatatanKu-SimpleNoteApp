package id.julham.catatanku.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import id.julham.catatanku.R
import id.julham.catatanku.base.BaseActivity
import id.julham.catatanku.databinding.ActivityLoginBinding
import id.julham.catatanku.ui.home.HomeActivity
import id.julham.catatanku.ui.splash.SplashActivity
import id.julham.catatanku.utils.hide
import id.julham.catatanku.utils.show
import id.julham.catatanku.utils.snackbar

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_login

    private val RC_SIGN_IN = 1
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        //get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.logIn.setOnClickListener { signIn() }
    }

    private fun signIn() {
        binding.progressBar.show()
        binding.logIn.hide()

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //result returned from launching the Intent from mGoogleSignInClient.signInIntent
        if (requestCode == RC_SIGN_IN) {
            //GoogleSignIn Task
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)

                //authenticating with firebase
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                binding.root.snackbar("Something's wrong $e")
                binding.progressBar.hide()
                binding.logIn.show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        //sign in using Firebase
        SplashActivity.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        overridePendingTransition(
                            R.anim.fade_in_animation,
                            R.anim.fade_out_animation
                        )
                    }
                } else {
                    binding.root.snackbar("Authentication failed ${task.exception.toString()}")
                    binding.progressBar.hide()
                    binding.logIn.show()
                }
            }
    }
}