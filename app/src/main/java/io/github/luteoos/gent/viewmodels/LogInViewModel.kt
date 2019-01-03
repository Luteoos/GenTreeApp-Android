package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.Parameters

class LogInViewModel: BaseViewModel() {

    fun LogInRest(username: String, password: String){
        //todo call rest , save to sessionmanager, use vmmessage to put info
        when(username){
            "True" -> message.value = Parameters.LOG_IN_SUCCESS
            "False" -> message.value = Parameters.LOG_IN_FAILED
        }
    }
}