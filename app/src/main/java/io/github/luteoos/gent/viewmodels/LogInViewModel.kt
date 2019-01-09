package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.network.api.request.authUserLogIn
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.Parameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class LogInViewModel: BaseViewModel() {

    fun LogInRest(username: String, password: String){
        val client = RestApi.createService(UserApi::class.java)
        disposable.add(client.authenticateUser(authUserLogIn(username,password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200){
                    SessionManager.userUUID = UUID.fromString(it.body()?.id)
                    SessionManager.username = it.body()?.username
                    SessionManager.accessToken = it.body()?.token
                    message.value = Parameters.LOG_IN_SUCCESS
                }
            },{
                message.value = Parameters.LOG_IN_FAILED
            }))
    }
}