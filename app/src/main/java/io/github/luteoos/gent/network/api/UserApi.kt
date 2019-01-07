package io.github.luteoos.gent.network.api

import io.github.luteoos.gent.network.api.request.authUserLogIn
import io.github.luteoos.gent.network.api.response.authUserLogInResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("api/users/auth")
    fun authenticateUser(@Body authUser: authUserLogIn) : Single<Response<authUserLogInResponse>>
}