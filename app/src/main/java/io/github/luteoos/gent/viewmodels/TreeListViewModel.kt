package io.github.luteoos.gent.viewmodels

import android.arch.lifecycle.MutableLiveData
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.network.api.request.authUserLogIn
import io.github.luteoos.gent.network.api.response.getTreesListAssignedToUser
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.Parameters
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class TreeListViewModel: BaseViewModel() {

    val GET_TREE_FAILED = "GET_TREE_FAILED"
    val list : MutableLiveData<MutableList<getTreesListAssignedToUser>> = MutableLiveData()


    fun getListFromApi(){
        val client = RestApi.createService(UserApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.getTreesList(SessionManager.userUUDString!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.code()==200){
                    list.value = it.body()
                }else
                    message.value = GET_TREE_FAILED
            },{
                message.value = GET_TREE_FAILED
            }))
    }
}