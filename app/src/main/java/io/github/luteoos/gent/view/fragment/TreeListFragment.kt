package io.github.luteoos.gent.view.fragment

import android.os.Bundle
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import io.github.luteoos.gent.R
import io.github.luteoos.gent.viewmodels.TreeListPresenter

class TreeListFragment: BaseFragmentMVVM<TreeListPresenter>() {
    override fun getLayoutID(): Int = R.layout.fragment_trees_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = TreeListPresenter()
        this.connectToVMMessage()
        setBindings()
    }

    override fun onVMMessage(msg: String?) {
    }

    private fun setBindings(){

    }
}