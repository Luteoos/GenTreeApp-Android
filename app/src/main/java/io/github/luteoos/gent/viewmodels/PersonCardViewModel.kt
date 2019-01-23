package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.MediaApi
import io.github.luteoos.gent.network.api.PersonApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.network.api.request.userAvatar
import io.github.luteoos.gent.session.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PersonCardViewModel : BaseViewModel() {
    val AVATAR_LOAD_NO_AVATAR = "AVATAR_LOAD_NO_AVATAR"
    val AVATAR_LOAD_FAILED = "AVATAR_LOAD_FAILED"
    val FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED"
    val FILE_UPLOAD_SUCCESS = "FILE_UPLOAD_SUCCESS"
    val ERROR_COMMENT = "ERROR_COMMENT"
    val COMMENT_ADDED = "COMMENT_ADDED"

    val avatarURL: MutableLiveData<String> = MutableLiveData()
    val lastUUID : MutableLiveData<String> = MutableLiveData()

    fun getPersonAvatar(uuid: String){
        val client = RestApi.createService(PersonApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.getPersonAvatar(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                lastUUID.value = uuid
                when {
                    it.code() == 200 -> {
                        avatarURL.value = it.body()?.url
                    }
                    it.code() == 404 -> message.value = AVATAR_LOAD_NO_AVATAR
                    else -> message.value = AVATAR_LOAD_FAILED
                }
            }, {
                message.value = AVATAR_LOAD_FAILED
                lastUUID.value = uuid
            })
        )
    }

    fun uploadMediaToServer(file: File,id: String){
        val client = RestApi.createService(MediaApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.uploadMedia(
            MultipartBody.Part.createFormData("file", file.nameWithoutExtension, RequestBody.create(
                MediaType.parse("image/*"), file
            )))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200){
                    if(!it.body()?.id.isNullOrEmpty())
                        addMediaUUIDToPersonAvatar(it.body()?.id!!,id)
                    else
                        message.value = FILE_UPLOAD_FAILED
                }else
                    message.value = FILE_UPLOAD_FAILED
            },{
                message.value = FILE_UPLOAD_FAILED
            }))
    }

    private fun addMediaUUIDToPersonAvatar(uuid: String,personID: String){
        val client = RestApi.createService(PersonApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.putPersonAvatarUUID(personID, uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200)
                    message.value = FILE_UPLOAD_SUCCESS
                else
                    message.value = FILE_UPLOAD_FAILED
            },{
                if(it.message!!.contains("input at line 1 column 1"))
                    message.value = FILE_UPLOAD_SUCCESS
                else
                    message.value = FILE_UPLOAD_FAILED
            }))
    }

    fun addCommentToPerson(uuid: String, body: String){
        val client = RestApi.createService(PersonApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.addCommentToPerson(uuid,io.github.luteoos.gent.network.api.request.addCommentToPerson(body))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200)
                    message.value = COMMENT_ADDED
                else
                    message.value = ERROR_COMMENT
            },{
                message.value = ERROR_COMMENT
            }))
    }
}