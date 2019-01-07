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

class LogInViewModel: BaseViewModel() {

    fun LogInRest(username: String, password: String){
        //todo call rest , save to sessionmanager, use vmmessage to put info'
        val client = RestApi.createService(UserApi::class.java)
        disposable.add(client.authenticateUser(authUserLogIn(username,password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200){
                    it.body()?.id
                    it.body()?.username
                    message.value = Parameters.LOG_IN_SUCCESS
                }
            },{

            }))

        when(username){
            "True" -> message.value = Parameters.LOG_IN_SUCCESS
            "False" -> message.value = Parameters.LOG_IN_FAILED
        }
    }
}