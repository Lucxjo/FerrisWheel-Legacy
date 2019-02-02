package co.aplicared.ferriswheel

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_setup.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class SetupActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        mFirebaseAuth = FirebaseAuth.getInstance()

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()


        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        var text: String
        var dname = intent.getStringExtra("text")

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


        }

    }
}
