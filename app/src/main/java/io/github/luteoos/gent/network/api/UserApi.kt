package io.github.luteoos.gent.network.api

import io.github.luteoos.gent.network.api.request.authUserLogIn
import io.github.luteoos.gent.network.api.request.userAvatar
import io.github.luteoos.gent.network.api.response.authUserLogInResponse
import io.github.luteoos.gent.network.api.response.getTreesListAssignedToUser
import io.github.luteoos.gent.network.api.response.MediaDtoResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("api/users/auth")
    fun authenticateUser(@Body authUser: authUserLogIn) : Single<Response<authUserLogInResponse>>

    @GET("api/users/{id}/avatar")
    fun getUserAvatar(@Path("id") id: String ) : Single<Response<MediaDtoResponse>>

    @POST("api/users/avatar")
    fun addUserAvatar(@Body avatar: userAvatar) : Single<Response<Unit>>

    @GET("api/trees/user/{id}")
    fun getTreesList(@Path("id") id: String ) : Single<Response<MutableList<getTreesListAssignedToUser>>>
}