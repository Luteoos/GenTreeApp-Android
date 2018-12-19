package io.github.luteoos.gent.view.activity

import android.app.Application
import android.os.Bundle
import android.util.EventLog
import android.view.View
import com.eightbitlab.rxbus.Bus
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.viewmodels.LogInViewModel
import kotlinx.android.synthetic.main.activity_log_in.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk27.coroutines.onClick

class LogInActivity : BaseActivityMVVM<LogInViewModel>() {

    override fun getLayoutID(): Int = R.layout.activity_log_in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LogInViewModel()
        this.connectToVMMessage()
        setBinding()

    }

    override fun onVMMessage(msg: String?){
        Toasty.info(this@LogInActivity, msg!!).show()
    }

    fun hideProgressBar(){
        progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun onLogInSuccess(){
        hideProgressBar()
        check.check()
        startMainActivity()
    }

    private fun setBinding(){
        btnExit.onClick {
            onBackPressed()
        }
        cl_background.onClick {
            hideKeyboard()
            progressBar.visibility = View.INVISIBLE
            check.check()
            viewModel.test("TEST")
        }
        btnLogIn.onClick {
            if(isNetworkOnLine)
                Toasty.error(this@LogInActivity, R.string.api_error).show()
            else{
                showProgressBar()
            }
        }
    }

    private fun startMainActivity(){

    }
}