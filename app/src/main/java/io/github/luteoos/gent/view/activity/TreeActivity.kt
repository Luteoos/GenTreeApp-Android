package io.github.luteoos.gent.view.activity

import android.os.Bundle
import android.view.View
import com.eightbitlab.rxbus.Bus
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.utils.Event
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.viewmodels.TreeViewModel
import kotlinx.android.synthetic.main.activity_tree.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class TreeActivity : BaseActivityMVVM<TreeViewModel>() {
    private lateinit var treeUUID : String

    override fun getLayoutID(): Int = R.layout.activity_tree

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = TreeViewModel()
        connectToVMMessage()
        getDataFromIntent()
        setBindings()
        connectToBus()
        viewModel.getTreeFromRest(treeUUID)
    }

    override fun finish() {
        super.finish()
        PersonListFromTree.Clear()
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.GET_API_ERROR -> {
                switchSpinnerVisibility(false)
                Toasty.error(this, R.string.api_error)
            }
            viewModel.GET_LIST_SUCCESSFUL -> {
                //TODO here load first person in tree to fragment and create fragment
            }
        }
    }

    private fun setBindings(){
        ivBack.onClick {
            onBackPressed()
        }
    }

    private fun connectToBus(){
        Bus.observe<Event.MessageWithUUID>().subscribe{
            when(it.message){
                Parameters.SWITCH_TO_PERSON_WITH_UUID -> {
                    //TODO here switch person on card
                }
            }
        }
    }

    private fun getDataFromIntent(){
        treeUUID = intent.getStringExtra(Parameters.TREE_UUID)
        tvTreeName.text = intent.getStringExtra(Parameters.TREE_NAME)
    }

    private fun switchSpinnerVisibility(boolean: Boolean){
        when(boolean){
            true -> spinner_tree.visibility = View.VISIBLE
            false -> spinner_tree.visibility = View.GONE
        }
    }
}