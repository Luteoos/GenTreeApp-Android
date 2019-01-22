package io.github.luteoos.gent.network.api

import io.github.luteoos.gent.network.api.response.MediaDtoResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonApi {

    @GET("api/persons/{id}/media/avatar")
    fun getPersonAvatar(@Path("id") id: String) : Single<Response<MediaDtoResponse>>
}