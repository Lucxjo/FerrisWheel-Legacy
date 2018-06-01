package me.turtlecode.ferriswheel.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_sign_in.*
import me.turtlecode.ferriswheel.MainActivity
import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.util.FirestoreUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SignInActivity : AppCompatActivity() {

    private val signInProviders =
            listOf(AuthUI.IdpConfig.EmailBuilder()
                    .setAllowNewAccounts(true)
                    .setRequireName(true)
                    .build())

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sContButton.setOnClickListener {

            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(signInProviders)
                    .setLogo(R.mipmap.ic_launcher)
                    .setTheme(R.style.AppTheme)
                    .setPrivacyPolicyUrl("https://www.iubenda.com/privacy-policy/89605138")
                    .build()

            startActivityForResult(intent, RC_SIGN_IN)
        }

        sDataButton.onClick {
            browse("https://www.iubenda.com/privacy-policy/89605138", false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Setting up your account")

                FirestoreUtil.initCurrentUserFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                    progressDialog.dismiss()
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                            longToast("No network available")
                    ErrorCodes.UNKNOWN_ERROR ->
                        longToast(R.string.fui_error_unknown.toString())
                }
            }
        }
    }
}
