package io.github.luteoos.gent.network.api

import io.github.luteoos.gent.network.api.response.mediaUploadResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface MediaApi {

    @Multipart
    @POST("api/media")
    fun uploadMedia(@Part part: MultipartBody.Part ) : Single<retrofit2.Response<mediaUploadResponse>>
}