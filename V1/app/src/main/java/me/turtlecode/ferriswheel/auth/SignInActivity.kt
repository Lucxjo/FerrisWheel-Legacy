package me.turtlecode.ferriswheel.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*
import me.turtlecode.ferriswheel.MainActivity
import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.service.AppFirebaseInstanceIDService
import me.turtlecode.ferriswheel.util.FirestoreUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    val handler = Handler()
    val runnable = Runnable {

            relLayout2.visibility = View.VISIBLE
            buttonRL.visibility = View.VISIBLE
    }

    private val signInProviders =
            listOf(AuthUI.IdpConfig.EmailBuilder()
                    .setAllowNewAccounts(true)
                    .setRequireName(true)
                    .build())

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mAuth = FirebaseAuth.getInstance()

        mAuth.useAppLanguage()

        checkUser()

        loginButton.onClick {
            if (emailET.text == null)
                Toast.makeText(this@SignInActivity, com.firebase.ui.auth.R.string.fui_missing_email_address, Toast.LENGTH_SHORT).show()
            else if (passET.text == null) {
                Toast.makeText(this@SignInActivity, "Please enter your email address", Toast.LENGTH_LONG).show()

            } else
                signIn(emailET.text.toString(), passET.text.toString())

        }

        signupButton.onClick {
            if (emailET.text == null)
                Toast.makeText(this@SignInActivity, com.firebase.ui.auth.R.string.fui_missing_email_address, Toast.LENGTH_SHORT).show()
            else if (passET.text == null)
                Toast.makeText(this@SignInActivity, "Please enter your email address", Toast.LENGTH_SHORT).show()
            else
                signUp(emailET.text.toString(), passET.text.toString())
        }

        privButton.onClick {
            browse("https://www.iubenda.com/privacy-policy/89605138")
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

                val registrationToken = FirebaseInstanceId.getInstance().token

                AppFirebaseInstanceIDService.addTokenToFirestore(registrationToken)

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

    fun checkUser() {
        if (FirebaseAuth.getInstance().currentUser != null)
            startActivity(intentFor<MainActivity>().newTask().clearTask())
        else {
            handler.postDelayed(runnable, 1000)
        }
    }

    fun signIn(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("FW.Signin", "signInWithEmail:success")
                        startActivity(intentFor<MainActivity>().newTask().clearTask())

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("FW.Signin", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@SignInActivity, R.string.fui_trouble_signing_in,
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }

    }

    fun signUp(email: String, password: String) {

        var pDialg = indeterminateProgressDialog("Setting up your account...")

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(@NonNull task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            pDialg.dismiss()
                            val user = mAuth.currentUser

                            user?.sendEmailVerification()
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            toast("A verification email has been sent.")
                                        }
                                    }

                            startActivity(intentFor<MainActivity>().newTask().clearTask())

                        } else {
                            // If sign in fails, display a message to the user.
                            pDialg.dismiss()
                            Log.w("FW.Signin", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@SignInActivity, R.string.fui_email_account_creation_error,
                                    Toast.LENGTH_SHORT).show()

                        }
                    }
                })
    }
}
