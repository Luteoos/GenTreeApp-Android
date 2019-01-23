package io.github.luteoos.gent.network.api

import io.github.luteoos.gent.network.api.request.addCommentToPerson
import io.github.luteoos.gent.network.api.request.addMediaToPerson
import io.github.luteoos.gent.network.api.response.MediaDtoResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface PersonApi {

    @GET("api/persons/{id}/media/avatar")
    fun getPersonAvatar(@Path("id") id: String) : Single<Response<MediaDtoResponse>>

    @PUT("api/persons/{id}/media/{mediaID}/avatar")
    fun putPersonAvatarUUID(@Path("id")id: String, @Path("mediaID") mediaID: String) : Single<Response<Void>>

    @POST("api/persons/{id}/comments")
    fun addCommentToPerson(@Path("id") id: String, @Body comment: addCommentToPerson) : Single<Response<Unit>>

    @POST("api/persons/{id}/media")
    fun addMediaToPerson(@Path("id") id: String, @Body comment: addMediaToPerson) : Single<Response<Void>>

    @GET("api/persons/{id}/media")
    fun getPersonMedia(@Path("id") id: String) : Single<Response<MutableList<MediaDtoResponse>>>
}