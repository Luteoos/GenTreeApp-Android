package io.github.luteoos.gent.viewmodels

import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.Parameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class MyProfileFragmentViewModel : BaseViewModel() {

    val AVATAR_LOAD_SUCCESS = "AVATAR_LOAD_SUCCESS"
    val AVATAR_LOAD_FAILED = "AVATAR_LOAD_FAILED"
    var avatarByteString: String? = null

    fun getUserAvatarByte() {
        avatarByteString = null

        val client = RestApi.createService(UserApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.getUserAvatar(SessionManager.userUUDString!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200){
                    avatarByteString = it.body()?.content
                    message.value = AVATAR_LOAD_SUCCESS
                }else
                    message.value = AVATAR_LOAD_FAILED
            },{
                message.value = AVATAR_LOAD_FAILED
            }))
    }
}