package me.turtlecode.ferriswheel.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import me.turtlecode.ferriswheel.AppConstants
import me.turtlecode.ferriswheel.ChatActivity
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_messages.*

import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.R.id.mRecycle
import me.turtlecode.ferriswheel.recyclerview.item.PersonItem
import me.turtlecode.ferriswheel.util.FirestoreUtil


class MessagesFragment : Fragment() {
    private lateinit var userListenerRegistration: ListenerRegistration
    private var shoulInitRecyclerView = true
    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        userListenerRegistration = FirestoreUtil.addUsersListener(this.activity!!, this::updateRecyclerView)

        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FirestoreUtil.removeListener(userListenerRegistration)
        shoulInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {

            mRecycle.apply {
                layoutManager = LinearLayoutManager(this@MessagesFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {

                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)

                } as RecyclerView.Adapter<*>
            }

            shoulInitRecyclerView = false

        }

        fun update() {

        }

        if (shoulInitRecyclerView)
            init()
        else
            update()

    }
    private val onItemClick = OnItemClickListener { item, view ->
        if (item is PersonItem) {


            startActivity(Intent(this@MessagesFragment.context, ChatActivity::class.java).putExtra(AppConstants.USER_ID, item.userId))
        }
    }
}
