package io.github.luteoos.gent.network.api.dataobjects

class DetailsDto(var id: String,
                 var name: String,
                 var surname: String,
                 var sex: String,
                 var comments: MutableList<CommentDto>,
                 var events: MutableList<EventDto>) {
}