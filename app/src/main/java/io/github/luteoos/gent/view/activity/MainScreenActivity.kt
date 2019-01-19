package io.github.luteoos.gent.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.MediaApi
import io.github.luteoos.gent.view.fragment.MyProfileFragment
import io.github.luteoos.gent.view.fragment.TreeListFragment
import io.github.luteoos.gent.view.fragment.WelcomeProfileFragment
import io.github.luteoos.gent.viewmodels.MainScreenViewModel
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main_screen.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainScreenActivity : BaseActivityMVVM<MainScreenViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainScreenViewModel()
        this.connectToVMMessage()
        setBindings()
        showWelcomeFragment()
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

    private fun showWelcomeFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment, WelcomeProfileFragment())
            .commit()
    }

    private fun showTreesFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment, TreeListFragment())
            .commit()
    }

    private fun showProfileFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment, MyProfileFragment())
            .commit()
    }
}