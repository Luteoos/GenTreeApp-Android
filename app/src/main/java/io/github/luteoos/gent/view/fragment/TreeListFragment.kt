package io.github.luteoos.gent.view.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.response.getTreesListAssignedToUser
import io.github.luteoos.gent.view.recyclerviews.RVTreesList
import io.github.luteoos.gent.viewmodels.TreeListViewModel
import kotlinx.android.synthetic.main.fragment_trees_list.*
import org.jetbrains.anko.support.v4.ctx

class TreeListFragment: BaseFragmentMVVM<TreeListViewModel>() {
    override fun getLayoutID(): Int = R.layout.fragment_trees_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = TreeListViewModel()
        this.connectToVMMessage()
        setBindings()
        viewModel.getListFromApi()
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.GET_TREE_FAILED -> {
                Toasty.error(ctx, R.string.api_error)
                ivError.visibility = View.VISIBLE
                treeListSpinner.visibility = View.GONE
            }
        }
    }

    private fun setBindings(){
        viewModel.list.observe(this, Observer {
            if(it != null)
                setRVList(it)
        })
    }

    private fun setRVList(list: MutableList<getTreesListAssignedToUser>){
        ivError.visibility = View.GONE
        treeListSpinner.visibility = View.GONE
        rvTree.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RVTreesList(list, context)
        }
        val adapter = rvTree.adapter!! as RVTreesList
        adapter.intent.observe(this, Observer {
            if(it != null)
                startActivity(it)
        })
    }
}