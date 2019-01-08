package me.turtlecode.ferriswheel.model

import java.util.*

data class TextMessage(val text: String,
                       override val time: Date,
                       override val senderId: String,
                       override val recipientID: String,
                       override val senderName: String,
                       override val type: String = MessageType.TEXT)
    : Message{

    constructor() : this("", Date(0), "", "","")
}