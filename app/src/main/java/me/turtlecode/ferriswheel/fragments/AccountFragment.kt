package me.turtlecode.ferriswheel.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*

import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.auth.SignInActivity
import me.turtlecode.ferriswheel.util.FirestoreUtil
import me.turtlecode.ferriswheel.util.FirestoreUtil.getCurrentUser
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AccountFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.apply {
            aChip.visibility = View.INVISIBLE

            aSaveButton.onClick {
                FirestoreUtil.updateCurrentUser(
                        aNameText.text.toString(),
                        0,
                        aGSpinner.selectedItemPosition,
                        aCSpinner.selectedItemPosition,
                        aRSpinner.selectedItemPosition,
                        aPhoneText.text.toString())
                aChip.visibility = View.VISIBLE

                Handler().postDelayed({
                    aChip.visibility = View.INVISIBLE
                },5000)
            }

            aSignOutButton.onClick {
                AuthUI.getInstance()
                        .signOut(this@AccountFragment.context!!)
                        .addOnCompleteListener {
                            startActivity(Intent(AccountFragment().context, SignInActivity::class.java))
                        }
            }

        }

        return view
    }

    override fun onStart() {
        super.onStart()

        getCurrentUser { user ->
            if (this@AccountFragment.isVisible) {
                aNameText.setText(user.name)
                aGSpinner.setSelection(user.gender)
                aCSpinner.setSelection(user.orientation)
                aRSpinner.setSelection(user.relationship)
                aPhoneText.setText(user.phoneNumber)
            }
        }
    }


}
