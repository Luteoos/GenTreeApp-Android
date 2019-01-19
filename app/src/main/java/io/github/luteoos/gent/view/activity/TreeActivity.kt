package io.github.luteoos.gent.view.activity

import android.os.Bundle
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import io.github.luteoos.gent.R
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
        viewModel.getTreeFromRest(treeUUID)
    }

    override fun onVMMessage(msg: String?) {

    }

    private fun setBindings(){
        ivBack.onClick {
            onBackPressed()
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