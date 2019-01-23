package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.MediaApi
import io.github.luteoos.gent.network.api.PersonApi
import io.github.luteoos.gent.network.api.response.MediaDtoResponse
import io.github.luteoos.gent.session.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PersonGalleryViewModel: BaseViewModel() {

    val GALLERY_LOAD_FAILED = "GALLERY_LOAD_FAILED"
    val GALLERY_LOAD_SUCCESS = "GALLERY_LOAD_SUCCESS"
    val FILE_UPLOAD_FAILED = "FILE_UPLOAD_FAILED"
    val FILE_UPLOAD_SUCCESS = "FILE_UPLOAD_SUCCESS"

    val mediaList: MutableLiveData<MutableList<MediaDtoResponse>> = MutableLiveData()

    fun getMediaList(uuid: String){
        val client = RestApi.createService(PersonApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.getPersonMedia(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when {
                    it.code() == 200 -> mediaList.value = it.body()
                    else -> message.value = GALLERY_LOAD_FAILED
                }
            }, {
                message.value = GALLERY_LOAD_FAILED
            })
        )
    }

    fun uploadFileToApi(file: File, uuid: String){
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
                        addMediaToPerson(it.body()?.id!!,uuid)
                    else
                        message.value = FILE_UPLOAD_FAILED
                }else
                    message.value = FILE_UPLOAD_FAILED
            },{
                message.value = FILE_UPLOAD_FAILED
            }))
    }

    private fun addMediaToPerson(mediaID: String, uuid: String){
        val client = RestApi.createService(PersonApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.addMediaToPerson(uuid, io.github.luteoos.gent.network.api.request.addMediaToPerson(mediaID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200)
                    message.value = FILE_UPLOAD_SUCCESS
                else
                    message.value = FILE_UPLOAD_FAILED
            },{
                    message.value = FILE_UPLOAD_FAILED
            }))
    }
}