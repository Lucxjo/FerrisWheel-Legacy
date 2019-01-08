package me.turtlecode.ferriswheel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO
import com.google.firebase.auth.FirebaseAuth
import me.turtlecode.ferriswheel.auth.SignInActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null)
            startActivity<MainActivity>()
        else
            startActivity<SignInActivity>()
        finish()
    }
}
