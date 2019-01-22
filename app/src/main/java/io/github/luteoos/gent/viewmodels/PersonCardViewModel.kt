package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.PersonApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.session.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PersonCardViewModel : BaseViewModel() {
    val AVATAR_LOAD_NO_AVATAR = "AVATAR_LOAD_NO_AVATAR"
    val AVATAR_LOAD_FAILED = "AVATAR_LOAD_FAILED"

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
}