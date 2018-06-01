package me.turtlecode.ferriswheel.recyclerview.item

import android.content.Context
import android.graphics.drawable.GradientDrawable
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.*
import me.turtlecode.ferriswheel.R
import me.turtlecode.ferriswheel.R.array.gender
import me.turtlecode.ferriswheel.model.User
import me.turtlecode.ferriswheel.util.StorageUtil


class PersonItem(val person: User,
                val userId: String,
                private val context: Context)
    : Item() {

    private lateinit var mGender: String
    private lateinit var mOrientation: String

    private val genders = arrayOf(R.array.gender.toString())
    private val orientations = arrayOf(R.array.orientation.toString())

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_id.text = userId

        if (person.gender == 0)
            mGender = genders[0]
        else if (person.gender == 1)
            mGender = genders[1]
        else if (person.gender == 2)
            mGender = genders[2]

        if (person.orientation == 0)
            mOrientation = orientations[0]
        else if (person.orientation == 1)
            mOrientation = orientations[1]
        else if (person.orientation == 2)
            mOrientation = orientations[2]
        else if (person.orientation == 3)
            mOrientation = orientations[3]

        viewHolder.textView_msg.text = "${mGender} looking for ${mOrientation}"
    }
    override fun getLayout() = R.layout.item_person
}

