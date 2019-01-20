package io.github.luteoos.gent.viewmodels

import com.eightbitlab.rxbus.Bus
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.TreeApi
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TreeViewModel : BaseViewModel() {

    val GET_API_ERROR = "GET_API_ERROR"
    val GET_LIST_SUCCESSFUL = "GET_LIST_SUCCESSFUL"

    fun getTreeFromRest(treeUUID: String){
        val client = RestApi.createService(TreeApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.getPersonsList(treeUUID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               if (it.code() == 200){
                   PersonListFromTree.list = it.body()
                   message.value = GET_LIST_SUCCESSFUL
               }else
                   message.value = GET_API_ERROR
            }, {
                message.value = GET_API_ERROR
            }))
    }
}