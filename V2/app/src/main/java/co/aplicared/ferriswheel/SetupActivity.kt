package co.aplicared.ferriswheel

import android.os.Bundle
import android.util.TypedValue
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setup.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class SetupActivity : AppCompatActivity() {

    val m = arrayOf("Jaques", "Blue", "Alec", "Magnus", "Hypnos")
    val fm = arrayOf("Athena", "Tyche", "Keelin", "Aline", "Iris")
    val nb = arrayOf("Atrians")

    //TODO: Check sOText is correct (*chat with*/chatting with)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        //sGender.onItemSelectedListener {
        //    setDisplayNameStrings()
        //}

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

    fun setDisplayNameStrings() {

        lateinit var gender: Array<String>
        if (sGender.selectedItem.toString() == R.array.gender.toString(0)) {
            gender = m
        } else if (sGender.selectedItem.toString() == R.array.gender.toString(1)) {
            gender = fm
        } else {
            gender = nb
        }

        sDis.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gender)
    }
}
