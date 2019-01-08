package me.turtlecode.ferriswheel.model

data class ChatChannel (val userIDs: MutableList<String>) {
    constructor() : this(mutableListOf())
}