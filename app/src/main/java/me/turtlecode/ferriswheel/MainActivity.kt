package me.turtlecode.ferriswheel


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.turtlecode.ferriswheel.fragments.AccountFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }


    @SuppressLint("CommitTransaction")
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}
