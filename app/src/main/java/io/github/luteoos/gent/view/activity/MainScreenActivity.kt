package io.github.luteoos.gent.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.view.fragment.MyProfileFragment
import io.github.luteoos.gent.viewmodels.MainScreenViewModel
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainScreenActivity : BaseActivityMVVM<MainScreenViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainScreenViewModel()
        this.connectToVMMessage()
        setBindings()
    }

    override fun getLayoutID(): Int = R.layout.activity_main_screen

    override fun onVMMessage(msg: String?) {
    }

    private fun setBindings(){
        btnUserProfile.onClick {
            showProfileFragment()
        }
        btnTreesList.onClick {
            showTreesFragment()
        }
    }

    private fun showTreesFragment(){

    }

    private fun showProfileFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment, MyProfileFragment())
            .commit()
    }
}