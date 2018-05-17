package me.turtlecode.ferriswheel

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO
import com.google.firebase.auth.FirebaseAuth
import me.turtlecode.ferriswheel.auth.SignInActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO)

        if (FirebaseAuth.getInstance().currentUser != null)
            startActivity<MainActivity>()
        else
            startActivity<SignInActivity>()
        finish()
    }
}
