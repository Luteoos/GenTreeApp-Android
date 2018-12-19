package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel

class LogInViewModel: BaseViewModel() {

    fun test(m: String){
        message.value = m
    }
}