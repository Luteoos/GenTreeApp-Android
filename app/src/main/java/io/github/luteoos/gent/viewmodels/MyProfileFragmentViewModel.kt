package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.MediaApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.network.api.request.userAvatar
import io.github.luteoos.gent.session.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MyProfileFragmentViewModel : BaseViewModel() {

    val AVATAR_LOAD_NO_AVATAR = "AVATAR_LOAD_NO_AVATAR"
    val AVATAR_LOAD_FAILED = "AVATAR_LOAD_FAILED"
    val FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED"
    val FILE_UPLOAD_SUCCESS = "FILE_UPLOAD_SUCCESS"
    val avatarURL: MutableLiveData<String> = MutableLiveData()

    fun getUserAvatarByte() {
        if(SessionManager.avatar != "")
            avatarURL.value = SessionManager.avatar
        else {
            val client = RestApi.createService(UserApi::class.java, SessionManager.accessToken!!)
            disposable.add(client.getUserAvatar(SessionManager.userUUDString!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when {
                        it.code() == 200 -> {
                            avatarURL.value = it.body()?.url
                            SessionManager.avatar = it.body()?.url
                        }
                        it.code() == 204 -> message.value = AVATAR_LOAD_NO_AVATAR
                        else -> message.value = AVATAR_LOAD_FAILED
                    }
                }, {
                    message.value = AVATAR_LOAD_FAILED
                })
            )
        }
    }

    fun uploadMediaToServer(file: File){
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
                        addMediaUUIDToAvatar(it.body()?.id!!)
                    else
                        message.value = FILE_UPLOAD_FAILED
                }else
                    message.value = FILE_UPLOAD_FAILED
            },{
                message.value = FILE_UPLOAD_FAILED
            }))
    }

    fun addMediaUUIDToAvatar(uuid: String){
        val client = RestApi.createService(UserApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.addUserAvatar(userAvatar(SessionManager.userUUDString!!, uuid))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200){
                    SessionManager.avatar = null
                    message.value = FILE_UPLOAD_SUCCESS
                }else
                    message.value = FILE_UPLOAD_FAILED
            },{
                message.value = FILE_UPLOAD_FAILED
            }))
    }
}