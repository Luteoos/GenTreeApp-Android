package io.github.luteoos.gent.network.api.dataobjects

class EventDto(var id: String, var date: String, var type: String, var description: String) {

    fun getFormattedDate(): String =  date.split("T")[0]
}