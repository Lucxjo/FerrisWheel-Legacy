package me.turtlecode.ferriswheel


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.turtlecode.ferriswheel.fragments.AccountFragment
import me.turtlecode.ferriswheel.fragments.MessagesFragment
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import io.fabric.sdk.android.Fabric
import me.turtlecode.ferriswheel.DSL.Verify
import me.turtlecode.ferriswheel.auth.SignInActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(mTopAppBar)
        setSupportActionBar(navigation)
        Fabric.with(this, Crashlytics())

        replaceFragment(MessagesFragment())

        /**
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_messages -> {
                    //TODO: Show messages fragment.
                    true
                }
                R.id.navigation_details -> {
                    replaceFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }
        */
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.navigation_messages) {
            replaceFragment(MessagesFragment())
        }

        if (item?.itemId == R.id.navigation_details) {
            replaceFragment(AccountFragment())
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
    }
}
