package co.aplicared.ferriswheel

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_setup.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class SetupActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var mAnalytics: FirebaseAnalytics
    private lateinit var mAuth: FirebaseAuth
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        mAnalytics = FirebaseAnalytics.getInstance(this)

        mAuth = FirebaseAuth.getInstance()

        remoteConfig = FirebaseRemoteConfig.getInstance()


        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        var text: String
        var dname = intent.getStringExtra("text")
        val firstSetup = intent.getBooleanExtra("firstSignUp", false)

        dname = "Louis"
        text = dname

        if (text == "co.aplicared.ferriswheel.LoginActivity.register") {
            text = getString(R.string.account_message)
            sPText.text = text
        } else {
            val msg = getString(R.string.account_return)
            val hi = getString(R.string.hi)
            text = "$hi $dname, $msg"
            sPText.text = text
            sPText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }

        sSave.onClick {
            val firstname = sName
            val lastname = sName2
            val fullname = "$firstname $lastname"

            if (firstSetup) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Crashlytics.log(Log.INFO, TAG, "User with password creation: success!")
                            val user = mAuth.currentUser
                            user!!.sendEmailVerification()

                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(sDisplay.text.toString())
                                .build()

                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        Log.d(TAG, "User profile updated.")
                                    }
                                }
                        } else {
                            Crashlytics.log(Log.WARN, TAG, "User with password creation: " + task.exception)
                        }
                    }
            }

        }

    }

    fun dialogMessage(msg: String) {
        lateinit var dialog: AlertDialog
        dialog = alert {
            customView {
                verticalLayout {
                    padding = dip(16)
                    textView(msg).textSize = 18f
                    textView(getString(android.R.string.ok)) {
                        this.setTextColor(getColor(R.color.colorPrimary))
                        this.textSize = 18f
                        this.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    }.lparams {
                        this.topMargin = 16
                        this.width = 150
                        this.gravity = Gravity.END

                    }.onClick {
                        dialog.dismiss()
                    }
                }
            }
        }.show() as AlertDialog
    }
}
