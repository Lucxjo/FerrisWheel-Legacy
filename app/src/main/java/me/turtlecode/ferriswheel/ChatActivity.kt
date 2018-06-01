package me.turtlecode.ferriswheel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import me.turtlecode.ferriswheel.model.MessageType
import me.turtlecode.ferriswheel.model.TextMessage
import me.turtlecode.ferriswheel.model.User
import me.turtlecode.ferriswheel.util.FirestoreUtil
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    private lateinit var currentChannelID: String
    private lateinit var currentUser: User
    private lateinit var otherUserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val chatState = intent.getStringExtra(AppConstants.CHAT_STATE)


        otherUserID = intent.getStringExtra(AppConstants.USER_ID)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(otherUserID + " (${chatState})")

        FirestoreUtil.getCurrentUser {
            currentUser = it
        }


        FirestoreUtil.getOrCreateChatChannel(otherUserID) {channelID ->
            messageListenerRegistration =
                    FirestoreUtil.addChatMessagesListener(channelID, this, this::updateRecyclerView)

            sendImageView.onClick {
                val messageToSend =
                        TextMessage(editTextMessage.text.toString(), Calendar.getInstance().time,
                                FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT)
                editTextMessage.setText("")

                FirestoreUtil.sendMessage(messageToSend, channelID)
            }

            fabSendImage.onClick {
                TODO("Send Image messages")
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            messagesRecyclerView.apply { layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {

                    messagesSection = Section(messages)
                    add(messagesSection)

                }
            }
            shouldInitRecyclerView = false
        }

        fun update() = messagesSection.update(messages)

        if (shouldInitRecyclerView) {
            init()
        } else
            update()
        messagesRecyclerView.scrollToPosition(messagesRecyclerView.adapter!!.itemCount - 1)
    }
}
