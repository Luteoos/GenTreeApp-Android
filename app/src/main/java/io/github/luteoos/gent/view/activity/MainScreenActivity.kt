package io.github.luteoos.gent.view.activity

import android.os.Bundle
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.viewmodels.MainScreenViewModel

class MainScreenActivity : BaseActivityMVVM<MainScreenViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainScreenViewModel()
        this.connectToVMMessage()
    }

    override fun getLayoutID(): Int = R.layout.activity_main_screen

    override fun onVMMessage(msg: String?) {
        Toasty.success(this,"ddddddd").show()
    }
}