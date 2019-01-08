package me.turtlecode.ferriswheel.recyclerview.item

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.*
import kotlinx.android.synthetic.main.notification_template_lines_media.view.*
import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.model.TextMessage
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

class TextMessageItem (val message: TextMessage,
                       val context: Context)
    : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView_message_text.text = message.text

        setTimeText(viewHolder)
        setMessageGravity(viewHolder)

    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

        viewHolder.textView_message_time.text = dateFormat.format(message.time)
    }

    private fun setMessageGravity(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {

            viewHolder.message_root.apply {
                backgroundResource = R.drawable.sent_bg
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)

                this.layoutParams = lParams
            }

        } else {
            viewHolder.message_root.apply {
                backgroundResource = R.drawable.recieved_bg
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)

                this.layoutParams = lParams
            }

        }
    }

    override fun getLayout() = R.layout.item_text_message
}