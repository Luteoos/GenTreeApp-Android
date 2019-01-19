package io.github.luteoos.gent.viewmodels

import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.data.TreeListPersons
import io.github.luteoos.gent.network.RestApi
import io.github.luteoos.gent.network.api.TreeApi
import io.github.luteoos.gent.network.api.UserApi
import io.github.luteoos.gent.session.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TreeViewModel : BaseViewModel() {


    fun getTreeFromRest(treeUUID: String){
        val client = RestApi.createService(TreeApi::class.java, SessionManager.accessToken!!)
        disposable.add(client.getPersonsList(treeUUID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               if (it.code() == 200){
                   TreeListPersons.list = it.body()
                   TreeListPersons.getPerson(it.body()?.get(0)?.id!!)
               }
            }, {
            }))
    }
}