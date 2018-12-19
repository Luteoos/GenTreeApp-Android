package io.github.luteoos.gent.view.activity

import android.app.Application
import android.os.Bundle
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.R
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.viewmodels.LogInViewModel
import kotlinx.android.synthetic.main.activity_log_in.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class LogInActivity : BaseActivityMVVM<LogInViewModel>() {

    override fun getLayoutID(): Int = R.layout.activity_log_in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LogInViewModel()
        setBinding()
    }

    private fun setBinding(){
        btnExit.onClick {
            onBackPressed()
        }
    }
}