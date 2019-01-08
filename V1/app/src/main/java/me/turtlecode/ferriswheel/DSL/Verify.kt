package me.turtlecode.ferriswheel.DSL

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_verify.*
import me.turtlecode.ferriswheel.R
import org.jetbrains.anko.sdk27.coroutines.onClick
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import me.turtlecode.ferriswheel.MainActivity
import me.turtlecode.ferriswheel.auth.SignInActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast


class Verify : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        vVerify.onClick {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    ?.addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(@NonNull task: Task<Void>) {
                            if (task.isSuccessful()) {
                                Log.d("FW Verify", "Email sent.")
                                toast("Email Sent")
                            }
                        }
                    })
        }

        vSignin.onClick {
            startActivity(intentFor<SignInActivity>().newTask().clearTask())
        }
    }
}
