package io.github.luteoos.gent.network.api

import io.github.luteoos.gent.network.api.dataobjects.PersonDTO
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TreeApi {

    @GET("api/Trees/{id}/persons")
    fun getPersonsList(@Path("id") id: String) : Single<Response<MutableList<PersonDTO>>>
}