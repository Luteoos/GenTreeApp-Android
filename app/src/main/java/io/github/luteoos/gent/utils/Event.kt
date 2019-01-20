package io.github.luteoos.gent.utils

import java.util.*

object Event {

    class MessageWithUUID(val uuid: UUID, val message: String)
    class MessageWithObject<out T>(val message: String, val value: T)
}